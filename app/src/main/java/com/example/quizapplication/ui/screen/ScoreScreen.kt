package com.example.quizapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizapplication.models.Question

@Composable
fun ScoreScreen(
    score: Int,
    totalQuestions: Int,
    questions: List<Question>,
    userAnswers: Map<Int, String>,
    onReviewTest: () -> Unit,
    onFeedback: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp)
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Quiz Completed!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Your Score", style = MaterialTheme.typography.titleMedium)
        Text(text = "$score / $totalQuestions", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(24.dp))

        // Red/Green Lights
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            questions.indices.forEach { index ->
                val isCorrect = questions[index].correctAnswer == userAnswers[index]
                val color = if (isCorrect) Color.Green else Color.Red
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onReviewTest, modifier = Modifier.fillMaxWidth()) {
            Text("Review the Test")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onFeedback, modifier = Modifier.fillMaxWidth()) {
            Text("AI Generated Feedback")
        }
    }
}