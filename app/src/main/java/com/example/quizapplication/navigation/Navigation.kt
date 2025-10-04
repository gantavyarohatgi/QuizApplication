package com.example.quizapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quizapplication.ui.QuizViewModel
import com.example.quizapplication.ui.screen.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

sealed class Screen(val route: String) {
    object Start : Screen("start_screen")
    object Waiting : Screen("waiting_screen")
    object Question : Screen("question_screen/{index}") {
        fun createRoute(index: Int) = "question_screen/$index"
    }
    object Score : Screen("score_screen")
    object Summary : Screen("summary_screen/{index}") {
        fun createRoute(index: Int) = "summary_screen/$index"
    }
    object Feedback : Screen("feedback_screen")
}

@Composable
fun NavGraph(navController: NavHostController, viewModel: QuizViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Start.route
    ) {
        composable(Screen.Start.route) {
            StartScreen(
                topicList = uiState.topicList,
                difficultyList = uiState.difficultyList,
                selectedTopic = uiState.selectedTopic,
                selectedDifficulty = uiState.selectedDifficulty,
                onTopicChange = viewModel::onTopicChange,
                onDifficultyChange = viewModel::onDifficultyChange,
                onStartClick = {
                    viewModel.startQuizGeneration()
                    navController.navigate(Screen.Waiting.route)
                }
            )
        }

        composable(Screen.Waiting.route) {
            WaitScreen(
                progress = uiState.waitScreenProgress,
                message = uiState.waitScreenMessage
            )
            LaunchedEffect(uiState.waitScreenProgress) {
                if (uiState.waitScreenProgress >= 1.0f && uiState.questions.isNotEmpty()) {
                    navController.navigate(Screen.Question.createRoute(0)) {
                        popUpTo(Screen.Start.route)
                    }
                }
            }
        }

        composable(
            route = Screen.Question.route,
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            QuestionScreen(
                question = uiState.questions.getOrNull(index),
                questionIndex = index,
                totalQuestions = uiState.questions.size,
                selectedAnswer = uiState.userAnswers[index],
                onAnswerSelected = { answer -> viewModel.onAnswerSelected(index, answer) },
                onNextClicked = { navController.navigate(Screen.Question.createRoute(index + 1)) },
                onPreviousClicked = { navController.popBackStack() },
                onSubmitClicked = {
                    viewModel.onSubmitQuiz()
                    navController.navigate(Screen.Score.route) {
                        popUpTo(Screen.Start.route)
                    }
                }
            )
        }

        composable(Screen.Score.route) {
            ScoreScreen(
                score = uiState.finalScore,
                totalQuestions = uiState.questions.size,
                questions = uiState.questions,
                userAnswers = uiState.userAnswers,
                onReviewTest = { navController.navigate(Screen.Summary.createRoute(0)) },
                onFeedback = {
                    // 1. Call the ViewModel to start generating feedback
                    viewModel.generateFeedback()

                    // 2. Then, navigate to the feedback screen
                    navController.navigate(Screen.Feedback.route)
                }
            )
        }

        composable(
            route = Screen.Summary.route,
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            SummaryScreen(
                question = uiState.questions.getOrNull(index),
                questionIndex = index,
                totalQuestions = uiState.questions.size,
                userAnswer = uiState.userAnswers[index],
                onNextClicked = { navController.navigate(Screen.Summary.createRoute(index + 1)) },
                onPreviousClicked = { navController.popBackStack() },
                onFinishReview = { navController.popBackStack(Screen.Score.route, inclusive = false) }
            )
        }

        composable(Screen.Feedback.route) {
            FeedbackScreen(
                isLoading = uiState.isGeneratingFeedback,
                feedbackText = uiState.feedback,
                onDone = {
                    // Navigate back to the start screen for a new quiz
                    viewModel.startNewQuiz()
                    navController.navigate(Screen.Start.route) {
                        popUpTo(Screen.Start.route) { inclusive = true }
                    }
                }
            )
        }
    }

    // Show an error dialog if an error occurs
    if (uiState.error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissError() },
            title = { Text("An Error Occurred") },
            text = { Text(uiState.error!!) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.dismissError()
                    // Navigate back to the start screen on error
                    navController.popBackStack(Screen.Start.route, inclusive = false)
                }) {
                    Text("OK")
                }
            }
        )
    }
}