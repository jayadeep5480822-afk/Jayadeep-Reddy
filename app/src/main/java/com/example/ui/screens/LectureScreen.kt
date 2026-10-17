package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import com.example.ui.components.AiVideoAvatarPlayer
import com.example.ui.components.CheckpointDialog
import com.example.ui.components.InteractiveSimulationWidget
import com.example.util.SpeechHelper
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureScreen(
    topic: StudyTopic,
    language: Language,
    onLanguageChange: (Language) -> Unit,
    speechHelper: SpeechHelper,
    onStartMockTest: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler {
        speechHelper.stop()
        onBack()
    }

    var selectedCharacter by remember {
        mutableStateOf(CharacterRepository.getById(topic.leadCharacterId))
    }
    var currentSlideIndex by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    val isSpeaking by speechHelper.isSpeaking.collectAsState()

    // In-between checkpoint popup trigger
    var activeCheckpoint by remember { mutableStateOf<CheckpointQuestion?>(null) }
    val viewedCheckpoints = remember { mutableStateListOf<String>() }

    val currentSlide = topic.slides.getOrElse(currentSlideIndex) { topic.slides.first() }

    // Auto-trigger checkpoint question popup when reaching slide with checkpoint
    LaunchedEffect(currentSlideIndex) {
        val cp = currentSlide.checkpointQuestion
        if (cp != null && !viewedCheckpoints.contains(cp.id)) {
            delay(1200) // slight delay for natural intro before pop quiz
            activeCheckpoint = cp
            viewedCheckpoints.add(cp.id)
            isPlaying = false
            speechHelper.stop()
        }
    }

    // Auto-read lecture spoken dialogue via TTS when slide changes and playing is active
    fun speakCurrentSlide() {
        val dialogueText = currentSlide.spokenDialogue.get(language)
        speechHelper.speak(
            text = dialogueText,
            language = language,
            pitch = selectedCharacter.voicePitch,
            speechRate = selectedCharacter.voiceRate * playbackSpeed
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            speechHelper.stop()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = topic.title.get(language),
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
                    IconButton(onClick = {
                        speechHelper.stop()
                        onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Language Switcher Menu
                    var showLangMenu by remember { mutableStateOf(false) }
                    Box {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier
                                .clickable { showLangMenu = true }
                                .testTag("lecture_lang_toggle")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Translate, contentDescription = null, modifier = Modifier.size(16.dp))
                                Text(
                                    text = language.displayName,
                                    fontSize = 11.sp,
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
                                        if (isPlaying) speakCurrentSlide()
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Topic Complete?",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Take Higher Studies Mock Exam",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            speechHelper.stop()
                            onStartMockTest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.testTag("button_start_mock_test")
                    ) {
                        Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Mock Test", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // AI Professor Switcher
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AI Teaching Team:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CharacterRepository.allCharacters.forEach { char ->
                        val isSelected = selectedCharacter.id == char.id
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedCharacter = char
                                    if (isPlaying) speakCurrentSlide()
                                }
                                .testTag("select_char_${char.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = char.avatarRes),
                                    contentDescription = char.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .border(1.5.dp, Color(char.accentColorHex), CircleShape)
                                )
                                Column {
                                    Text(
                                        text = char.name.split(" ").last(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // AI Video Avatar Presentation Player
            item {
                AiVideoAvatarPlayer(
                    character = selectedCharacter,
                    currentSlide = currentSlide,
                    slideIndex = currentSlideIndex,
                    totalSlides = topic.slides.size,
                    isPlaying = isPlaying,
                    isSpeaking = isSpeaking,
                    playbackSpeed = playbackSpeed,
                    language = language,
                    onTogglePlay = {
                        isPlaying = !isPlaying
                        if (isPlaying) {
                            speakCurrentSlide()
                        } else {
                            speechHelper.stop()
                        }
                    },
                    onPreviousSlide = {
                        if (currentSlideIndex > 0) {
                            currentSlideIndex--
                            if (isPlaying) speakCurrentSlide()
                        }
                    },
                    onNextSlide = {
                        if (currentSlideIndex < topic.slides.size - 1) {
                            currentSlideIndex++
                            if (isPlaying) speakCurrentSlide()
                        }
                    },
                    onSpeedChange = {
                        playbackSpeed = it
                        if (isPlaying) speakCurrentSlide()
                    },
                    onTtsToggle = {
                        if (isSpeaking) {
                            speechHelper.stop()
                        } else {
                            speakCurrentSlide()
                        }
                    }
                )
            }

            // Interactive Live Example (if present on this slide)
            currentSlide.liveExample?.let { liveEx ->
                item {
                    InteractiveSimulationWidget(
                        example = liveEx,
                        language = language
                    )
                }
            }

            // Manual In-Between Checkpoint button if slide has a checkpoint
            currentSlide.checkpointQuestion?.let { cp ->
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { activeCheckpoint = cp }
                            .testTag("in_lecture_checkpoint_banner"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Quiz,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(28.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "In-Lecture Checkpoint Available",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Text(
                                    text = "Test your grasp on ${currentSlide.title.get(language)}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                                )
                            }
                            Button(
                                onClick = { activeCheckpoint = cp },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary
                                )
                            ) {
                                Text("Answer", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

    // Checkpoint Dialog Popup
    activeCheckpoint?.let { cp ->
        CheckpointDialog(
            question = cp,
            character = CharacterRepository.getById(cp.speakerId),
            language = language,
            onDismiss = {
                activeCheckpoint = null
            }
        )
    }
}
