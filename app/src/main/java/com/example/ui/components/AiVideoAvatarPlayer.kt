package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiCharacter
import com.example.model.Language
import com.example.model.LectureSlide

@Composable
fun AiVideoAvatarPlayer(
    character: AiCharacter,
    currentSlide: LectureSlide,
    slideIndex: Int,
    totalSlides: Int,
    isPlaying: Boolean,
    isSpeaking: Boolean,
    playbackSpeed: Float,
    language: Language,
    onTogglePlay: () -> Unit,
    onPreviousSlide: () -> Unit,
    onNextSlide: () -> Unit,
    onSpeedChange: (Float) -> Unit,
    onTtsToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pulse animation for avatar when speaking
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val avatarScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSpeaking) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ai_video_player_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F172A)
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Video Frame (AI Avatar + Live Classroom Stage)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1E1B4B),
                                Color(0xFF0F172A)
                            )
                        )
                    )
            ) {
                // Background subtle ambient grid lines
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: AI Character Avatar with Audio Ring
                    Box(
                        modifier = Modifier
                            .size(130.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Glowing animated ring
                        Box(
                            modifier = Modifier
                                .size(if (isSpeaking) 126.dp else 116.dp)
                                .scale(avatarScale)
                                .clip(CircleShape)
                                .background(
                                    Brush.sweepGradient(
                                        listOf(
                                            Color(character.accentColorHex),
                                            Color(0xFF06B6D4),
                                            Color(0xFFA855F7),
                                            Color(character.accentColorHex)
                                        )
                                    )
                                )
                        )

                        // Avatar Image
                        Image(
                            painter = painterResource(id = character.avatarRes),
                            contentDescription = character.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(112.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color(0xFF0F172A), CircleShape)
                        )

                        // Live audio wave indicator badge
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .offset(y = 6.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSpeaking) Color(0xFF10B981) else Color(0xFF334155)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                if (isSpeaking) {
                                    WaveBar(heightMultiplier = 1.0f)
                                    WaveBar(heightMultiplier = 0.6f)
                                    WaveBar(heightMultiplier = 0.9f)
                                }
                                Text(
                                    text = if (isSpeaking) "SPEAKING" else "IDLE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Right: Character identity & Live subtitle preview
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(character.accentColorHex).copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color(character.accentColorHex).copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = character.title.get(language),
                                color = Color(character.accentColorHex),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Spoken Dialogue in Selected Language
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E293B).copy(alpha = 0.85f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "\"${currentSlide.spokenDialogue.get(language)}\"",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFE2E8F0),
                                fontSize = 11.sp,
                                maxLines = 4,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                // Top Floating Status Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Red.copy(alpha = 0.85f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Text(
                                text = "AI VIDEO LECTURE",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = "Slide ${slideIndex + 1} / $totalSlides",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Video Player Controls Bar
            Surface(
                color = Color(0xFF131B2E),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    // Slide progress scrubber
                    LinearProgressIndicator(
                        progress = { (slideIndex + 1) / totalSlides.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color(character.accentColorHex),
                        trackColor = Color(0xFF334155),
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous Slide
                        IconButton(
                            onClick = onPreviousSlide,
                            enabled = slideIndex > 0,
                            modifier = Modifier.testTag("button_prev_slide")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipPrevious,
                                contentDescription = "Previous Section",
                                tint = if (slideIndex > 0) Color.White else Color.Gray
                            )
                        }

                        // Play / Pause Primary Action
                        FilledIconButton(
                            onClick = onTogglePlay,
                            modifier = Modifier
                                .size(46.dp)
                                .testTag("button_play_pause"),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = Color(character.accentColorHex)
                            )
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        // Next Slide
                        IconButton(
                            onClick = onNextSlide,
                            enabled = slideIndex < totalSlides - 1,
                            modifier = Modifier.testTag("button_next_slide")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Next Section",
                                tint = if (slideIndex < totalSlides - 1) Color.White else Color.Gray
                            )
                        }

                        // TTS Voice Audio Toggle
                        IconButton(
                            onClick = onTtsToggle,
                            modifier = Modifier.testTag("button_tts_toggle")
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                contentDescription = "Toggle Audio",
                                tint = if (isSpeaking) Color(0xFF10B981) else Color.White
                            )
                        }

                        // Playback Speed Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF1E293B),
                            modifier = Modifier
                                .clickable {
                                    val nextSpeed = when (playbackSpeed) {
                                        0.75f -> 1.0f
                                        1.0f -> 1.25f
                                        1.25f -> 1.5f
                                        else -> 0.75f
                                    }
                                    onSpeedChange(nextSpeed)
                                }
                                .testTag("button_speed_toggle")
                        ) {
                            Text(
                                text = "${playbackSpeed}x",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            // Whiteboard Lecture Presentation
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = currentSlide.title.get(language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Bullet points
                currentSlide.whiteboardNotes.forEach { note ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            color = Color(character.accentColorHex),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = note.get(language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Math Formula or Code Block if present
                if (!currentSlide.equationOrCode.isNullOrBlank()) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E293B),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentSlide.equationOrCode,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = Color(0xFF38BDF8),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WaveBar(heightMultiplier: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val h by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 12f * heightMultiplier,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "h"
    )

    Box(
        modifier = Modifier
            .width(2.dp)
            .height(h.dp)
            .background(Color.White, RoundedCornerShape(1.dp))
    )
}
