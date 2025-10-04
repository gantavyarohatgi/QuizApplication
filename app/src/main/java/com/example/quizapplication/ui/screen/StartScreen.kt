package com.example.quizapplication.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    topicList: List<String>,
    difficultyList: List<String>,
    selectedTopic: String,
    selectedDifficulty: String,
    onTopicChange: (String) -> Unit,
    onDifficultyChange: (String) -> Unit,
    onStartClick: () -> Unit,
) {
    var topicExpanded by remember { mutableStateOf(false) }
    var difficultyExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp)
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Setup",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        ExposedDropdownMenuBox(
            expanded = topicExpanded,
            onExpandedChange = { topicExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.Companion.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                value = selectedTopic,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Topic") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = topicExpanded) },
            )
            ExposedDropdownMenu(
                expanded = topicExpanded,
                onDismissRequest = { topicExpanded = false }
            ) {
                topicList.forEach { topic ->
                    DropdownMenuItem(
                        text = { Text(topic) },
                        onClick = {
                            onTopicChange(topic)
                            topicExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = difficultyExpanded,
            onExpandedChange = { difficultyExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.Companion.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                value = selectedDifficulty,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Difficulty") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = difficultyExpanded) },
            )
            ExposedDropdownMenu(
                expanded = difficultyExpanded,
                onDismissRequest = { difficultyExpanded = false }
            ) {
                difficultyList.forEach { difficulty ->
                    DropdownMenuItem(
                        text = { Text(difficulty) },
                        onClick = {
                            onDifficultyChange(difficulty)
                            difficultyExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onStartClick,
            enabled = selectedTopic.isNotBlank() && selectedDifficulty.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Start Quiz")
        }
    }
}