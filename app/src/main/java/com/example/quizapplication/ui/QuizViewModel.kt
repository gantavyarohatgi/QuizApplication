package com.example.quizapplication.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapplication.models.Question
import com.example.quizapplication.models.QuizUiState
import com.example.quizapplication.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class QuizViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val TAG = "QuizViewModel"
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    // Companion object for parsing JSON. Helps avoid re-creating the parser.
    companion object {
        private val jsonParser = Json { isLenient = true; ignoreUnknownKeys = true }
    }

    init {
        loadInitialSettings()
    }

    private fun loadInitialSettings() {
        val topics = listOf("History", "Science", "Movies", "Geography")
        val difficulties = listOf("Easy", "Medium", "Hard")
        _uiState.update {
            it.copy(
                topicList = topics,
                difficultyList = difficulties,
                selectedTopic = topics.first(),
                selectedDifficulty = difficulties.first()
            )
        }
    }

    fun onTopicChange(topic: String) { _uiState.update { it.copy(selectedTopic = topic) } }
    fun onDifficultyChange(difficulty: String) { _uiState.update { it.copy(selectedDifficulty = difficulty) } }

    fun startQuizGeneration() {
        viewModelScope.launch {
            // Step 1: Set initial loading state
            _uiState.update {
                it.copy(waitScreenProgress = 0.1f, waitScreenMessage = "Warming up the AI...")
            }

            try {
                // Step 2: Build the prompt
                val topic = _uiState.value.selectedTopic
                val difficulty = _uiState.value.selectedDifficulty
                val prompt = buildQuizPrompt(topic, difficulty)

                _uiState.update { it.copy(waitScreenProgress = 0.4f, waitScreenMessage = "Generating questions...") }

                // Step 3: Call the API
                val response = generativeModel.generateContent(prompt)

                for(i in 0..10){
                    _uiState.update { it.copy(waitScreenProgress = 0.4f + 0.02f * i, waitScreenMessage = "Generating questions...") }
                }

                _uiState.update { it.copy(waitScreenProgress = 0.8f, waitScreenMessage = "Finalizing...") }

                // Step 4: Parse the response
                val responseText = response.text ?: throw SerializationException("Response text is null")
                val questions = jsonParser.decodeFromString<List<Question>>(responseText)

                // Step 5: Update state with success
                _uiState.update {
                    it.copy(
                        questions = questions,
                        waitScreenProgress = 1.0f,
                        waitScreenMessage = "Let's begin!"
                    )
                }
            } catch (e: Exception) {
                // Step 6: Handle any errors

                Log.e(TAG, "Error generating quiz", e)
                Log.d(TAG, "API KEY: ${BuildConfig.GEMINI_API_KEY}")

                _uiState.update {
                    it.copy(
                        error = "Failed to generate quiz: ${e.message}",
                        waitScreenProgress = 0f
                    )
                }
            }
        }
    }

    fun onAnswerSelected(questionIndex: Int, answer: String) {
        val updatedAnswers = _uiState.value.userAnswers.toMutableMap()
        updatedAnswers[questionIndex] = answer
        _uiState.update { it.copy(userAnswers = updatedAnswers) }
    }

    fun onSubmitQuiz() {
        var correctAnswers = 0
        val questions = _uiState.value.questions
        val userAnswers = _uiState.value.userAnswers
        for (i in questions.indices) {
            if (questions[i].correctAnswer == userAnswers[i]) {
                correctAnswers++
            }
        }
        _uiState.update { it.copy(finalScore = correctAnswers) }
    }

    private fun buildQuizPrompt(topic: String, difficulty: String): String {
        return """
        You are a helpful quiz creation assistant. Your task is to generate exactly 5 quiz questions.
        The topic is "$topic" and the difficulty is "$difficulty".

        Please respond ONLY with a valid JSON array of 5 question objects.
        Do not include any other text, explanations, or markdown formatting like ```json.

        Each JSON object must have the following exact structure:
        - "questionText": A string containing the question.
        - "options": An array of exactly 4 strings representing the multiple-choice answers.
        - "correctAnswer": A string that exactly matches one of the items in the "options" array.
        - "hint": A short string providing a hint for the question.
        - "summary": A brief, one-sentence explanation of the correct answer.

        Here is an example of the required format for a single question object:
        {
          "questionText": "What is the capital of Japan?",
          "options": ["Beijing", "Seoul", "Tokyo", "Bangkok"],
          "correctAnswer": "Tokyo",
          "hint": "It's a major city on the island of Honshu.",
          "summary": "Tokyo, Japan's bustling capital, mixes the ultramodern and the traditional."
        }
        
        Now, generate the JSON array for the topic "$topic" and difficulty "$difficulty".
        """.trimIndent()
    }

    fun generateFeedback() {
        viewModelScope.launch {
            // Set loading state
            _uiState.update { it.copy(isGeneratingFeedback = true) }

            try {
                val questions = _uiState.value.questions
                val userAnswers = _uiState.value.userAnswers

                val prompt = buildFeedbackPrompt(questions, userAnswers)
                val response = generativeModel.generateContent(prompt)

                // Update state with the generated feedback
                _uiState.update {
                    it.copy(
                        isGeneratingFeedback = false,
                        feedback = response.text
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error generating feedback", e)
                _uiState.update {
                    it.copy(
                        isGeneratingFeedback = false,
                        feedback = "Sorry, there was an error generating your feedback."
                    )
                }
            }
        }
    }

    private fun buildFeedbackPrompt(questions: List<Question>, userAnswers: Map<Int, String>): String {
        val analysisStringBuilder = StringBuilder()
        analysisStringBuilder.append("You are an encouraging AI quiz tutor. Your task is to provide personalized feedback to a student based on their quiz results. Analyze each question and their answer.\n\n")
        analysisStringBuilder.append("Here are the quiz results:\n")

        questions.forEachIndexed { index, question ->
            val userAnswer = userAnswers[index] ?: "Not answered"
            val isCorrect = userAnswer == question.correctAnswer
            analysisStringBuilder.append("- Question ${index + 1}: \"${question.questionText}\"\n")
            analysisStringBuilder.append("  - Correct Answer: ${question.correctAnswer}\n")
            analysisStringBuilder.append("  - Student's Answer: $userAnswer (${if (isCorrect) "Correct" else "Incorrect"})\n")
        }

        analysisStringBuilder.append("\nBased on these results, provide overall feedback in 2-3 paragraphs. Start by praising them for their correct answers. Then, gently point out the topics or questions they struggled with and offer a brief, encouraging tip or explanation. Address the user directly as 'you'. Do not use markdown.")

        return analysisStringBuilder.toString()
    }

    /**
     * Resets the quiz state for a new session.
     */
    fun startNewQuiz() {
        // Keep the topic/difficulty lists but reset everything else
        val topics = _uiState.value.topicList
        val difficulties = _uiState.value.difficultyList
        _uiState.value = QuizUiState(
            topicList = topics,
            difficultyList = difficulties,
            selectedTopic = topics.first(),
            selectedDifficulty = difficulties.first()
        )
    }

    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
}