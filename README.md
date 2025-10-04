# 🤖 AI QuizBot

AI QuizBot is a dynamic, intelligent quiz application for Android built with Jetpack Compose and powered by the Google Gemini API. Instead of static, pre-programmed questions, this app generates a unique set of 5 questions in real-time based on the user's chosen topic and difficulty. It provides a complete quiz experience, from question generation to scoring and personalized, AI-generated feedback on the user's performance.



---
## ✨ Key Features

* *Dynamic Quiz Generation*: Fetches 5 unique questions from the Gemini API for any topic and difficulty.
* *Personalized AI Feedback*: After the quiz, the AI analyzes your answers and provides encouraging, personalized feedback on your performance.
* *Clean, Modern UI*: Built entirely with Jetpack Compose, following Material Design 3 principles.
* *State-driven & Reactive*: Uses a ViewModel and StateFlow to manage UI state in a predictable, lifecycle-aware way.
* *Navigation Compose*: Handles all screen transitions and argument passing between different app screens.
* *Secure API Key Handling*: Demonstrates the best practice for storing and accessing API keys using local.properties and BuildConfig.

---
## 📸 Screenshots

| Start Screen | Loading Screen | Question Screen | Score Screen | Answers | AI Feedback Screen |
| :---: | :---: | :---: | :---: | :---: | :---: |
| ![Start Screen](https://github.com/gantavyarohatgi/QuizApplication/blob/main/images/1.jpeg?raw=true) | ![Loading Screen](https://github.com/gantavyarohatgi/QuizApplication/blob/main/images/2.jpeg?raw=true) | ![Question Screen](https://github.com/gantavyarohatgi/QuizApplication/blob/main/images/4.jpeg?raw=true) | ![Score Screen](https://github.com/gantavyarohatgi/QuizApplication/blob/main/images/5.jpeg?raw=true) | ![Answers](https://github.com/gantavyarohatgi/QuizApplication/blob/main/images/7.jpeg?raw=true) | ![AI Feedback Screen](https://github.com/gantavyarohatgi/QuizApplication/blob/main/images/8.jpeg?raw=true) |

---
## 🛠️ Tech Stack & Architecture

* *UI*: [Jetpack Compose](https://developer.android.com/jetpack/compose)
* *AI Model*: [Google Gemini API](https://ai.google.dev/)
* *Architecture*: Model-View-ViewModel (MVVM)
* *State Management*: StateFlow with Unidirectional Data Flow (UDF)
* *Navigation*: [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
* *Asynchronous Programming*: Kotlin Coroutines
* *JSON Parsing*: [KotlinX Serialization](https://github.com/Kotlin/kotlinx.serialization)
* *API Client*: Ktor (via the official Gemini SDK)

---
## 🚀 Setup & Configuration

To build and run this project yourself, you need to provide your own Gemini API key.

### 1. Get a Gemini API Key

* Go to **[Google AI Studio](https://aistudio.google.com/)**.
* Click *"Get API key"* and create a new key.
* Copy the generated API key.

### 2. Store the API Key

* In the *root directory* of the project, create a new file named local.properties.
* Add your API key to this file like so:
    properties
    GEMINI_API_KEY="YOUR_API_KEY_HERE"
    
    This file is included in .gitignore by default to prevent you from accidentally committing your secret key.

### 3. Build the Project

* Open the project in Android Studio.
* The app/build.gradle.kts file is already configured to read this key from local.properties and make it available in the app via BuildConfig.
* *Sync Gradle*, and then run the app on an emulator or a physical device.

---
## 💡 Key Learning Points

This project is a practical demonstration of several important Android development concepts:
* Integrating a powerful LLM (Gemini) into a mobile application.
* Crafting effective prompts to get structured JSON output from an AI.
* Implementing a multi-screen app using Navigation Compose with arguments.
* Managing complex UI state with a central ViewModel and a single state holder (QuizUiState).
* Handling asynchronous operations and error states gracefully.

---
## 📄 License

This project is licensed under the MIT License. See the LICENSE file for details.
