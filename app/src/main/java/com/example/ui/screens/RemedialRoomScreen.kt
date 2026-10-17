package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.network.GeminiStudyService
import com.example.util.SpeechHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemedialRoomScreen(
    topic: StudyTopic,
    result: UserMockTestResult,
    language: Language,
    onLanguageChange: (Language) -> Unit,
    speechHelper: SpeechHelper,
    geminiService: GeminiStudyService,
    onRetakeTest: () -> Unit,
    onBackToHome: () -> Unit
) {
    BackHandler { onBackToHome() }

    val coroutineScope = rememberCoroutineScope()
    val questions = topic.mockTest.questions
    val isSpeaking by speechHelper.isSpeaking.collectAsState()

    // Doubt query state: questionId -> conversation
    val followUpAnswers = remember { mutableStateMapOf<String, String>() }
    val isAskingDoubt = remember { mutableStateMapOf<String, Boolean>() }
    val doubtInputs = remember { mutableStateMapOf<String, String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "AI Remedial Coaching Room",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Comprehensive Wrong-Answer Review",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackToHome) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                },
                actions = {
                    // Language selector dropdown / pill
                    var showLangMenu by remember { mutableStateOf(false) }
                    Box {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier
                                .clickable { showLangMenu = true }
                                .testTag("remedial_lang_toggle")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Translate, contentDescription = null, modifier = Modifier.size(16.dp))
                                Text(
                                    text = language.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showLangMenu,
                            onDismissRequest = { showLangMenu = false }
                        ) {
                            Language.entries.forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text("${lang.displayName} (${lang.nativeName})") },
                                    onClick = {
                                        onLanguageChange(lang)
                                        showLangMenu = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 6.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onRetakeTest,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("button_retake_test")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Retake Test")
                    }

                    Button(
                        onClick = onBackToHome,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("button_finish_remedial")
                    ) {
                        Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Finish")
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Score Summary Card
            item {
                val percentage = if (result.totalQuestions > 0) (result.score * 100) / result.totalQuestions else 0
                val leadChar = CharacterRepository.getById(topic.leadCharacterId)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (percentage >= 70) Color(0xFF065F46) else Color(0xFF831843)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(74.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$percentage%",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (percentage >= 70) "Concept Mastery Achieved!" else "Remedial Coaching Active",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Score: ${result.score} / ${result.totalQuestions} questions correct. ${result.totalQuestions - result.score} questions analyzed below.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            // Questions Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detailed Question Breakdown",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Explanations in ${language.displayName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Questions List with AI Character Detailed Analysis
            items(questions.size) { qIdx ->
                val q = questions[qIdx]
                val selectedOpt = result.selectedAnswers[qIdx]
                val isCorrect = selectedOpt == q.correctIndex
                val char = CharacterRepository.getById(q.remedialExplanation.speakerId)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("remedial_question_card_$qIdx"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        if (isCorrect) 1.dp else 2.dp,
                        if (isCorrect) Color(0xFF10B981) else Color(0xFFEF4444)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Status badge & Question Number
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Q${q.questionNumber} (${q.examTag})",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isCorrect) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                        contentDescription = null,
                                        tint = if (isCorrect) Color(0xFF10B981) else Color(0xFFEF4444),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (isCorrect) "CORRECT" else "INCORRECT",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (isCorrect) Color(0xFF10B981) else Color(0xFFEF4444)
                                    )
                                }
                            }
                        }

                        // Question Text
                        Text(
                            text = q.questionText.get(language),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )

                        // Options Review
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            q.options.forEachIndexed { optIdx, optText ->
                                val isChosen = selectedOpt == optIdx
                                val isRight = optIdx == q.correctIndex

                                val bgColor = when {
                                    isRight -> Color(0xFF10B981).copy(alpha = 0.15f)
                                    isChosen -> Color(0xFFEF4444).copy(alpha = 0.15f)
                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                }
                                val borderColor = when {
                                    isRight -> Color(0xFF10B981)
                                    isChosen -> Color(0xFFEF4444)
                                    else -> Color.Transparent
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = bgColor,
                                    border = BorderStroke(1.dp, borderColor),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "${('A' + optIdx)}. ${optText.get(language)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = if (isRight || isChosen) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (isRight) {
                                            Text("✓ Correct", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                                        } else if (isChosen) {
                                            Text("✗ Your Answer", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                                        }
                                    }
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        // AI Character Remedial Explanation Pod
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(char.accentColorHex).copy(alpha = 0.08f),
                            border = BorderStroke(1.dp, Color(char.accentColorHex).copy(alpha = 0.3f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Speaker Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = char.avatarRes),
                                            contentDescription = char.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(CircleShape)
                                                .border(2.dp, Color(char.accentColorHex), CircleShape)
                                        )
                                        Column {
                                            Text(
                                                text = "${char.name}'s Remedial Analysis",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color(char.accentColorHex)
                                            )
                                            Text(
                                                text = char.specialty.get(language),
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    // TTS Listen Button
                                    IconButton(
                                        onClick = {
                                            val textToRead = "${char.name} explains: ${q.remedialExplanation.whyCorrect.get(language)}. ${q.remedialExplanation.coreIntuition.get(language)}"
                                            speechHelper.speak(textToRead, language, char.voicePitch, char.voiceRate)
                                        },
                                        modifier = Modifier.testTag("tts_remedial_button_$qIdx")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = "Listen to AI Professor",
                                            tint = Color(char.accentColorHex)
                                        )
                                    }
                                }

                                // 1. Trap Breakdown (If user selected wrong option)
                                if (!isCorrect && selectedOpt != null && selectedOpt < q.remedialExplanation.trapAnalysis.size) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFEF4444).copy(alpha = 0.1f),
                                        border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.4f))
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(
                                                text = "⚠️ Why Option ${('A' + selectedOpt)} was a Common Trap:",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = Color(0xFFEF4444)
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = q.remedialExplanation.trapAnalysis[selectedOpt].get(language),
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }

                                // 2. Rigorous Derivation / Why Option is Correct
                                Column {
                                    Text(
                                        text = "🎯 Derivation & Correct Logic:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = q.remedialExplanation.whyCorrect.get(language),
                                        style = MaterialTheme.typography.bodySmall,
                                        lineHeight = 18.sp
                                    )
                                }

                                // 3. Fundamental Intuition
                                Column {
                                    Text(
                                        text = "💡 Core Intuition for Higher Studies:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = q.remedialExplanation.coreIntuition.get(language),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                // 4. Exam Mnemonic / Shortcut Rule
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = q.remedialExplanation.mnemonicOrExamTip.get(language),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }

                                // 5. Interactive Follow-Up Doubt Box
                                val qId = q.id
                                val followUpAnswer = followUpAnswers[qId]
                                val isBusy = isAskingDoubt[qId] == true

                                AnimatedVisibility(visible = !followUpAnswer.isNullOrBlank()) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF1E293B)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(
                                                text = "💬 ${char.name} answers your doubt:",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = Color(0xFF38BDF8)
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = followUpAnswer ?: "",
                                                fontSize = 11.sp,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    var currentText by remember { mutableStateOf(doubtInputs[qId] ?: "") }
                                    OutlinedTextField(
                                        value = currentText,
                                        onValueChange = {
                                            currentText = it
                                            doubtInputs[qId] = it
                                        },
                                        placeholder = {
                                            Text(
                                                text = "Ask ${char.name} a doubt...",
                                                fontSize = 11.sp
                                            )
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("doubt_input_$qIdx"),
                                        shape = RoundedCornerShape(10.dp),
                                        singleLine = true
                                    )

                                    FilledIconButton(
                                        onClick = {
                                            if (currentText.isNotBlank()) {
                                                isAskingDoubt[qId] = true
                                                val query = currentText
                                                currentText = ""
                                                doubtInputs[qId] = ""
                                                coroutineScope.launch {
                                                    val resp = geminiService.askFollowUpDoubt(
                                                        questionContext = q.questionText.en,
                                                        userQuery = query,
                                                        language = language,
                                                        character = char
                                                    )
                                                    followUpAnswers[qId] = resp
                                                    isAskingDoubt[qId] = false
                                                }
                                            }
                                        },
                                        enabled = currentText.isNotBlank() && !isBusy,
                                        modifier = Modifier.testTag("doubt_send_button_$qIdx")
                                    ) {
                                        if (isBusy) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(16.dp),
                                                strokeWidth = 2.dp,
                                                color = Color.White
                                            )
                                        } else {
                                            Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
