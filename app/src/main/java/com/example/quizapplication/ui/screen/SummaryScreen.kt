package com.example.quizapplication.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quizapplication.models.Question

@Composable
fun SummaryScreen(
    question: Question?,
    questionIndex: Int,
    totalQuestions: Int,
    userAnswer: String?,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onFinishReview: () -> Unit
) {
    if (question == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Question data not available.")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp)
            .padding(bottom = 30.dp)
    ) {
        // Top Progress Bar
        LinearProgressIndicator(
            progress = { (questionIndex + 1) / totalQuestions.toFloat() },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Reviewing Question ${questionIndex + 1} of $totalQuestions",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Question Text
        Text(
            text = question.questionText,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Options with indicators
        question.options.forEach { option ->
            val isCorrectAnswer = option == question.correctAnswer
            val isUserAnswer = option == userAnswer

            val (icon, color, fontWeight) = when {
                // ✅ Correct answer (and user chose it)
                isCorrectAnswer -> Triple(Icons.Default.CheckCircle, MaterialTheme.colorScheme.primary, FontWeight.Bold)
                // ❌ User's incorrect answer
                isUserAnswer && !isCorrectAnswer -> Triple(Icons.Default.Cancel, MaterialTheme.colorScheme.error, FontWeight.Normal)
                // Other options
                else -> Triple(null, MaterialTheme.colorScheme.onSurface, FontWeight.Normal)
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(imageVector = it, contentDescription = null, tint = color)
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    color = color,
                    fontWeight = fontWeight,
                    modifier = if (icon == null) Modifier.padding(start = 40.dp) else Modifier
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🧠 AI Generated Summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Explanation", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(question.summary, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Pushes buttons to the bottom

        // Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = onPreviousClicked,
                enabled = questionIndex > 0
            ) {
                Text("Previous")
            }
            Button(
                onClick = {
                    if (questionIndex == totalQuestions - 1) {
                        onFinishReview()
                    } else {
                        onNextClicked()
                    }
                }
            ) {
                Text(if (questionIndex == totalQuestions - 1) "Finish Review" else "Next")
            }
        }
    }
}