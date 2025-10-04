package com.example.quizapplication.models

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    val hint: String,
    val summary: String
)

data class QuizUiState(
    val topicList: List<String> = emptyList(),
    val difficultyList: List<String> = emptyList(),
    val selectedTopic: String = "",
    val selectedDifficulty: String = "",
    val waitScreenProgress: Float = 0f,
    val waitScreenMessage: String = "Preparing your quiz...",
    val questions: List<Question> = emptyList(),
    val userAnswers: Map<Int, String> = emptyMap(),
    val finalScore: Int = 0,
    val error: String? = null,
    val isGeneratingFeedback: Boolean = false,
    val feedback: String? = null
)