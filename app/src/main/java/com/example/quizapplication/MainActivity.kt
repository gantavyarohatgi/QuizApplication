package com.example.quizapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.quizapplication.navigation.NavGraph
import com.example.quizapplication.ui.QuizViewModel
import com.example.quizapplication.ui.theme.QuizApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizApplicationTheme {
                val navController = rememberNavController()
                val viewModel: QuizViewModel = viewModel()
                NavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}