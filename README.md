# 🤖 AI QuizBot

AI QuizBot is a dynamic, intelligent quiz application for Android built with Jetpack Compose and powered by the Google Gemini API. Instead of static, pre-programmed questions, this app generates a unique set of 5 questions in real-time based on the user's chosen topic and difficulty. It provides a complete quiz experience, from question generation to scoring and personalized, AI-generated feedback on the user's performance.

---

## 1. Project Setup & Demo

To run locally:
- Open the project in Android Studio.
- Ensure you have a Gemini API key ([Google AI Studio](https://aistudio.google.com/)).
- Add the API key to `local.properties` as follows:
  ```
  GEMINI_API_KEY="YOUR_API_KEY_HERE"
  ```
- Sync Gradle and run the app on an emulator or device.

---

## 2. Problem Understanding

The goal was to create a quiz app that dynamically generates questions and feedback using an AI model rather than relying on hardcoded data. The app should cover multiple topics and difficulty levels, deliver instant results, and provide personalized AI feedback. Assumptions:
- Users want quizzes on a variety of subjects.
- Feedback should be encouraging and educational.
- Mobile-first, but easily extensible.

---

## 3. AI Prompts & Iterations

### Prompts Used
- **Quiz Generation Prompt:**  
  The app sends a prompt to Gemini such as:
  > You are a helpful quiz creation assistant. Your task is to generate exactly 5 quiz questions.  
  > The topic is "Science" and the difficulty is "Medium".  
  > Please respond ONLY with a valid JSON array of 5 question objects.  
  > Each object includes: questionText, options, correctAnswer, hint, summary.

- **Feedback Generation Prompt:**  
  After the quiz, the app asks Gemini:
  > You are an encouraging AI quiz tutor. Provide personalized feedback based on the student's answers.

### Refinements Made
- Initially, responses from Gemini included markdown or extra text. The prompt was refined to request only raw JSON output.
- Added strict instructions for JSON schema to reduce parsing errors.
- Feedback prompt tweaked to emphasize positive, actionable advice.

---

## 4. Architecture & Code Structure

- **Navigation:** Managed by Jetpack Compose's Navigation component (see `App.kt`).
- **Screens:** Separate composable screens for start, loading, questions, score, summary (review), and feedback.
- **AI Service:** The `QuizViewModel` contains all logic for building prompts and calling Gemini (see `QuizViewModel.kt`).
- **State Management:** Centralized in a `QuizUiState` data class, held in a ViewModel and distributed via StateFlow (see `QuizModels.kt`).
- **UI:** Material 3, fully declarative Compose.
- **Async & API:** Gemini API via Ktor, async handled by Kotlin Coroutines.

---

## 5. Screenshots / Screen Recording

| Start Screen | Loading Screen | Question Screen | Score Screen | Answers | AI Feedback Screen |
| :---: | :---: | :---: | :---: | :---: | :---: |
| ![Start Screen](https://github.com/gantavyarohatgi/QuizApplication/blob/main/images/1.jpeg?raw=true) | ![Loading Screen](https://github.com/gantavyarohatgi/QuizApplication/blob/main/images/2.jpeg?raw=true) | ![Question Screen](https://github.com/gantavyarohatgi/QuizApplication/blob/main/images/4.jpeg?raw=true) | ![Score Screen](https://github.com/gantavyarohatgi/QuizApplication/blob/main/images/5.jpeg?raw=true) | ![Answers](https://github.com/gantavyarohatgi/QuizApplication/blob/main/images/7.jpeg?raw=true) | ![AI Feedback Screen](https://github.com/gantavyarohatgi/QuizApplication/blob/main/images/8.jpeg?raw=true) |

---

## 6. Known Issues / Improvements

- Sometimes, Gemini API returns malformed JSON or extra text, causing parse errors.
- Per-question hints to be implemented.
- Feedback may be generic if answers are skipped or not matched in the prompt.
- Error handling is basic; could be improved with retries and user-facing error messages.
- Dark mode is not fully polished.
- Question topics/difficulties are fixed; future versions could allow custom topics.
- The themes need to be improved to make the app more user friendly.

---

## 7. Bonus Work

- Animated progress/loading and score indicators.
- Per-question review with explanations.
- Personalized feedback card with scrollable content.
- Secure API key storage using `local.properties`.

---

## 🛠️ Tech Stack & Architecture

* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **AI Model:** [Google Gemini API](https://ai.google.dev/)
* **Architecture:** Model-View-ViewModel (MVVM)
* **State Management:** StateFlow with Unidirectional Data Flow (UDF)
* **Navigation:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
* **Asynchronous Programming:** Kotlin Coroutines
* **JSON Parsing:** [KotlinX Serialization](https://github.com/Kotlin/kotlinx.serialization)
* **API Client:** Ktor (via the official Gemini SDK)

---

## 💡 Key Learning Points

- Integrating a powerful LLM (Gemini) into a mobile application.
- Crafting effective prompts for structured JSON output.
- Implementing a multi-screen app using Navigation Compose.
- Managing complex UI state with a single ViewModel and state holder.
- Handling async operations and error states gracefully.

---

## 📄 License

This project is licensed under the MIT License. See the LICENSE file for details.
