package com.example.quizapplication.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FeedbackScreen(
    isLoading: Boolean,
    feedbackText: String?,
    onDone: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 30.dp)
                .padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Personalized Feedback",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                // Show a loading indicator while feedback is being generated
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "🤖 Your AI tutor is analyzing your results...",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            } else {
                // Show the generated feedback
                Card(
                    modifier = Modifier
                        .weight(4f) // Takes up available space
                        .fillMaxSize(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Text(
                        text = feedbackText ?: "Could not generate feedback.",
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()) // Make text scrollable
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Pushes button to the bottom

            // Button to navigate back to the start
            Button(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start a New Quiz")
            }
        }
    }
}