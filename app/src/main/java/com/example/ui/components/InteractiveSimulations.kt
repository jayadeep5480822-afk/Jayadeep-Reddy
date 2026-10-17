package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.model.LiveExample
import com.example.model.SimulationKind
import kotlin.math.exp
import kotlin.math.sin

@Composable
fun InteractiveSimulationWidget(
    example: LiveExample,
    language: Language,
    modifier: Modifier = Modifier
) {
    var param1 by remember(example.id) { mutableFloatStateOf(example.parameter1Default) }
    var param2 by remember(example.id) { mutableFloatStateOf(example.parameter2Default) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_simulation_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = example.title.get(language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = example.description.get(language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "LIVE LAB",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Visualizer Canvas based on simulation kind
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFF0F172A), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                when (example.simulationKind) {
                    SimulationKind.NEURAL_NETWORK -> {
                        NeuralDescentCanvas(learningRate = param1, momentum = param2)
                    }
                    SimulationKind.PHARMA_CLEARANCE -> {
                        PharmaClearanceCanvas(clearance = param1, volumeOfDist = param2)
                    }
                    else -> {
                        GenericOscillationCanvas(param1 = param1, param2 = param2)
                    }
                }
            }

            // Formula banner
            if (example.formulaDisplay.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1E293B),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Formula: ${example.formulaDisplay}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = Color(0xFF38BDF8)
                        )
                    }
                }
            }

            // Slider 1
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = example.parameter1Label.get(language),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "%.2f".format(param1),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Slider(
                    value = param1,
                    onValueChange = { param1 = it },
                    valueRange = example.parameter1Min..example.parameter1Max,
                    modifier = Modifier.testTag("slider_param1")
                )
            }

            // Slider 2
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = example.parameter2Label.get(language),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "%.2f".format(param2),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Slider(
                    value = param2,
                    onValueChange = { param2 = it },
                    valueRange = example.parameter2Min..example.parameter2Max,
                    modifier = Modifier.testTag("slider_param2")
                )
            }
        }
    }
}

@Composable
fun NeuralDescentCanvas(learningRate: Float, momentum: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "descent")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "t"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Draw loss contours
        val center = Offset(w * 0.7f, h * 0.6f)
        for (r in listOf(w * 0.12f, w * 0.25f, w * 0.4f, w * 0.55f)) {
            drawCircle(
                color = Color(0xFF1E3A8A).copy(alpha = 0.4f),
                radius = r,
                center = center,
                style = Stroke(width = 1.5f)
            )
        }

        // Draw descent trajectory curve
        val path = Path()
        val startX = w * 0.15f
        val startY = h * 0.2f
        path.moveTo(startX, startY)

        val steps = 40
        var prevX = startX
        var prevY = startY

        val oscillation = (learningRate * 60f) * (1f - momentum * 0.6f)
        for (i in 1..steps) {
            val progress = i / steps.toFloat()
            val curX = startX + (center.x - startX) * progress + sin(progress * 14f) * oscillation * (1f - progress)
            val curY = startY + (center.y - startY) * progress + (1f - progress) * (oscillation * 0.5f)
            path.lineTo(curX, curY)
            prevX = curX
            prevY = curY
        }

        drawPath(
            path = path,
            color = Color(0xFF06B6D4),
            style = Stroke(width = 3f)
        )

        // Animated descending ball
        val ballProgress = time
        val ballX = startX + (center.x - startX) * ballProgress + sin(ballProgress * 14f) * oscillation * (1f - ballProgress)
        val ballY = startY + (center.y - startY) * ballProgress + (1f - ballProgress) * (oscillation * 0.5f)

        // Minimum target point
        drawCircle(
            color = Color(0xFF10B981),
            radius = 8f,
            center = center
        )

        // Trajectory ball
        drawCircle(
            color = Color(0xFFF59E0B),
            radius = 9f,
            center = Offset(ballX, ballY)
        )
    }
}

@Composable
fun PharmaClearanceCanvas(clearance: Float, volumeOfDist: Float) {
    val ke = clearance / volumeOfDist.coerceAtLeast(1f) // elimination rate constant
    val tHalf = 0.693f / ke.coerceAtLeast(0.001f)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Axes
        drawLine(
            color = Color(0xFF475569),
            start = Offset(40f, h - 25f),
            end = Offset(w - 20f, h - 25f),
            strokeWidth = 2f
        )
        drawLine(
            color = Color(0xFF475569),
            start = Offset(40f, 20f),
            end = Offset(40f, h - 25f),
            strokeWidth = 2f
        )

        // Therapeutic window safe zone band
        val safeTop = h * 0.35f
        val safeBottom = h * 0.7f
        drawRect(
            color = Color(0xFF10B981).copy(alpha = 0.15f),
            topLeft = Offset(40f, safeTop),
            size = androidx.compose.ui.geometry.Size(w - 60f, safeBottom - safeTop)
        )

        // Concentration decay curve: C(t) = C0 * exp(-ke * t)
        val path = Path()
        val c0 = (h - 40f) * 0.85f
        val startY = (h - 25f) - c0
        path.moveTo(40f, startY)

        val points = 60
        for (i in 1..points) {
            val t = (i / points.toFloat()) * 24f // 24 hours
            val ct = c0 * exp(-ke * t * 0.8f)
            val px = 40f + (w - 70f) * (i / points.toFloat())
            val py = (h - 25f) - ct
            path.lineTo(px, py)
        }

        drawPath(
            path = path,
            color = Color(0xFFA855F7),
            style = Stroke(width = 3.5f)
        )
    }
}

@Composable
fun GenericOscillationCanvas(param1: Float, param2: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "osc")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val path = Path()
        path.moveTo(0f, h / 2f)

        val steps = 80
        for (i in 1..steps) {
            val x = (i / steps.toFloat()) * w
            val y = (h / 2f) + sin((i / 10f) * param1 + phase) * (h * 0.35f * param2)
            path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = Color(0xFF38BDF8),
            style = Stroke(width = 3f)
        )
    }
}
