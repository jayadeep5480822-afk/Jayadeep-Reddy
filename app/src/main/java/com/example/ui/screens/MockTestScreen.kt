package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockTestScreen(
    topic: StudyTopic,
    language: Language,
    onFinishTest: (UserMockTestResult) -> Unit,
    onBack: () -> Unit
) {
    val questions = topic.mockTest.questions
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() } // questionIndex -> optionIndex
    var remainingSeconds by remember { mutableIntStateOf(topic.mockTest.durationMinutes * 60) }
    var showConfirmSubmit by remember { mutableStateOf(false) }

    BackHandler {
        showConfirmSubmit = true
    }

    // Timer countdown
    LaunchedEffect(Unit) {
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        }
        // Auto-submit when time expires
        val score = userAnswers.count { (qIdx, optIdx) ->
            qIdx < questions.size && optIdx == questions[qIdx].correctIndex
        }
        onFinishTest(
            UserMockTestResult(
                topicId = topic.id,
                totalQuestions = questions.size,
                score = score,
                selectedAnswers = userAnswers.toMap(),
                timeTakenSeconds = (topic.mockTest.durationMinutes * 60) - remainingSeconds
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = topic.mockTest.title.get(language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = topic.field,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { showConfirmSubmit = true }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Timer pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (remainingSeconds < 120) Color(0xFFEF4444).copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer,
                        border = BorderStroke(1.dp, if (remainingSeconds < 120) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (remainingSeconds < 120) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary
                            )
                            val mins = remainingSeconds / 60
                            val secs = remainingSeconds % 60
                            Text(
                                text = "%02d:%02d".format(mins, secs),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (remainingSeconds < 120) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 6.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            if (currentQuestionIndex > 0) currentQuestionIndex--
                        },
                        enabled = currentQuestionIndex > 0,
                        modifier = Modifier.testTag("button_prev_question")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Previous")
                    }

                    if (currentQuestionIndex < questions.size - 1) {
                        Button(
                            onClick = { currentQuestionIndex++ },
                            modifier = Modifier.testTag("button_next_question")
                        ) {
                            Text("Next")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    } else {
                        Button(
                            onClick = { showConfirmSubmit = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF10B981)
                            ),
                            modifier = Modifier.testTag("button_submit_test")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Submit Exam", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (questions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No questions found for this topic.")
            }
            return@Scaffold
        }

        val question = questions[currentQuestionIndex]
        val selectedOption = userAnswers[currentQuestionIndex]

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Progress Bar & Question Tabs
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = question.examTag,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = { (currentQuestionIndex + 1) / questions.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }
            }

            // Question Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = question.questionText.get(language),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 24.sp
                        )
                    }
                }
            }

            // Options List
            items(question.options.size) { optIdx ->
                val option = question.options[optIdx]
                val isSelected = selectedOption == optIdx

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            userAnswers[currentQuestionIndex] = optIdx
                        }
                        .testTag("mock_option_$optIdx"),
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = ('A' + optIdx).toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Text(
                            text = option.get(language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            // Quick Question Selector Grid
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Question Grid:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    questions.forEachIndexed { idx, _ ->
                        val isAnswered = userAnswers.containsKey(idx)
                        val isCurrent = idx == currentQuestionIndex

                        Surface(
                            shape = CircleShape,
                            color = when {
                                isCurrent -> MaterialTheme.colorScheme.primary
                                isAnswered -> Color(0xFF10B981)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .clickable { currentQuestionIndex = idx }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${idx + 1}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent || isAnswered) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Confirm Submission Dialog
    if (showConfirmSubmit) {
        val answeredCount = userAnswers.size
        AlertDialog(
            onDismissRequest = { showConfirmSubmit = false },
            title = { Text("Submit Mock Test?") },
            text = {
                Text("You have answered $answeredCount out of ${questions.size} questions. Once submitted, your AI Character will review all incorrect answers with deep remedial explanations.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmSubmit = false
                        val score = userAnswers.count { (qIdx, optIdx) ->
                            qIdx < questions.size && optIdx == questions[qIdx].correctIndex
                        }
                        onFinishTest(
                            UserMockTestResult(
                                topicId = topic.id,
                                totalQuestions = questions.size,
                                score = score,
                                selectedAnswers = userAnswers.toMap(),
                                timeTakenSeconds = (topic.mockTest.durationMinutes * 60) - remainingSeconds
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    modifier = Modifier.testTag("confirm_submit_button")
                ) {
                    Text("Submit & Review")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmSubmit = false }) {
                    Text("Resume Test")
                }
            }
        )
    }
}
