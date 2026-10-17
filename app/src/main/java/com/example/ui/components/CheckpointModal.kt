package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Quiz
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
import androidx.compose.ui.window.Dialog
import com.example.model.AiCharacter
import com.example.model.CheckpointQuestion
import com.example.model.Language

@Composable
fun CheckpointDialog(
    question: CheckpointQuestion,
    character: AiCharacter,
    language: Language,
    onDismiss: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var hasAnswered by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("checkpoint_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header with Character Avatar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = character.avatarRes),
                        contentDescription = character.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(character.accentColorHex), CircleShape)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = question.promptHeading.get(language),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(character.accentColorHex),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${character.name}'s Pop Quiz",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = null,
                        tint = Color(character.accentColorHex)
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                // Question Text
                Text(
                    text = question.questionText.get(language),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Options List
                question.options.forEachIndexed { index, option ->
                    val isSelected = selectedIndex == index
                    val isCorrectOption = index == question.correctIndex

                    val (containerColor, borderColor, textColor) = when {
                        !hasAnswered -> {
                            if (isSelected) {
                                Triple(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            } else {
                                Triple(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        isCorrectOption -> {
                            Triple(
                                Color(0xFF10B981).copy(alpha = 0.2f),
                                Color(0xFF10B981),
                                MaterialTheme.colorScheme.onSurface
                            )
                        }
                        isSelected -> {
                            Triple(
                                Color(0xFFEF4444).copy(alpha = 0.2f),
                                Color(0xFFEF4444),
                                MaterialTheme.colorScheme.onSurface
                            )
                        }
                        else -> {
                            Triple(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                Color.Transparent,
                                MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !hasAnswered) {
                                selectedIndex = index
                                hasAnswered = true
                            }
                            .testTag("checkpoint_option_$index"),
                        shape = RoundedCornerShape(12.dp),
                        color = containerColor,
                        border = BorderStroke(1.5.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (hasAnswered && isCorrectOption) Color(0xFF10B981) else Color(character.accentColorHex).copy(alpha = 0.2f),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = ('A' + index).toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (hasAnswered && isCorrectOption) Color.White else Color(character.accentColorHex)
                                    )
                                }
                            }

                            Text(
                                text = option.get(language),
                                style = MaterialTheme.typography.bodyMedium,
                                color = textColor,
                                modifier = Modifier.weight(1f)
                            )

                            if (hasAnswered) {
                                if (isCorrectOption) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = Color(0xFF10B981),
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = "Incorrect",
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Feedback panel
                AnimatedVisibility(visible = hasAnswered) {
                    val wasCorrect = selectedIndex == question.correctIndex
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (wasCorrect) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFF59E0B).copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, if (wasCorrect) Color(0xFF10B981) else Color(0xFFF59E0B))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = if (wasCorrect) "🎉 ${character.name}:" else "💡 ${character.name}'s Guidance:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (wasCorrect) Color(0xFF10B981) else Color(0xFFF59E0B)
                            )
                            Text(
                                text = if (wasCorrect) question.correctFeedback.get(language) else question.wrongFeedback.get(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Action Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("checkpoint_continue_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(character.accentColorHex)
                    )
                ) {
                    Text(
                        text = if (hasAnswered) "Resume Lecture" else "Skip Question",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
