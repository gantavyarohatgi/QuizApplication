package com.example.quizapplication.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapplication.models.Question

@Composable
fun QuestionScreen(
    question: Question?,
    questionIndex: Int,
    totalQuestions: Int,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onSubmitClicked: () -> Unit
) {
    if (question == null) {
        // Show a loading or error state if question is unexpectedly null
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
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

        Spacer(modifier = Modifier.height(16.dp))

        // Question Text
        Text(
            text = question.questionText,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Options
        question.options.forEach { option ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == selectedAnswer),
                        onClick = { onAnswerSelected(option) }
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedAnswer),
                    onClick = { onAnswerSelected(option) }
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
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
                        onSubmitClicked()
                    } else {
                        onNextClicked()
                    }
                },
                enabled = selectedAnswer != null
            ) {
                Text(if (questionIndex == totalQuestions - 1) "Submit" else "Next")
            }
        }
    }
}