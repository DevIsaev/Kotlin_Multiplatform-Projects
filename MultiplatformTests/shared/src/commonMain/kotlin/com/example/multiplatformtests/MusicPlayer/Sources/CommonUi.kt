package com.example.multiplatformtests.MusicPlayer.Sources

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import musicplayerkmp.shared.generated.resources.Res
import musicplayerkmp.shared.generated.resources.cover
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

// Переиспользуемые "кирпичики" — квадрат под обложку/клип и полоса прогресса
@Composable
fun ArtworkSquare(size: Dp, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.cover),
        contentDescription = null,
        modifier = Modifier.size(size)
    )
}

@Composable
fun FlippableArtwork(
    size: Dp,
    isFlipped: Boolean,
    onClick: () -> Unit,
    front: @Composable () -> Unit,
    back: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "cardFlip"
    )

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12 * density // "глубина" 3D-перспективы
            }
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
    ) {
        if (rotation <= 90f) {
            front()
        } else {
            // компенсируем зеркалирование задней стороны
            Box(modifier = Modifier.graphicsLayer { rotationY = 180f }) {
                back()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressBar(
    progress: Float,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    trackHeight: Dp = 2.dp,
    dotSize: Dp = 10.dp,
    activeColor: Color = Color.White,
    inactiveColor: Color = Color.White.copy(alpha = 0.25f)
) {
    var isDragging by remember { mutableStateOf(false) }
    var localProgress by remember(progress) { mutableStateOf(progress) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp) // область для удобного тапа/драга
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        onSeek(localProgress)
                    },
                    onHorizontalDrag = { change, _ ->
                        change.consume()
                        val newProgress = (change.position.x / size.width).coerceIn(0f, 1f)
                        localProgress = newProgress
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val newProgress = (offset.x / size.width).coerceIn(0f, 1f)
                    localProgress = newProgress
                    onSeek(newProgress)
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val currentProgress = if (isDragging) localProgress else progress

        // фон-линия
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .background(inactiveColor, RoundedCornerShape(50))
        )

        // активная часть линии
        Box(
            modifier = Modifier
                .fillMaxWidth(currentProgress)
                .height(trackHeight)
                .background(activeColor, RoundedCornerShape(50))
        )

        // точка — плавно появляется при драге
        val dotAlpha by animateFloatAsState(if (isDragging) 1f else 0f, label = "dotAlpha")
        val dotOffsetPx = (widthPx * currentProgress) - with(LocalDensity.current) { (dotSize / 2).toPx() }

        Box(
            modifier = Modifier
                .offset { IntOffset(dotOffsetPx.roundToInt(), 0) }
                .size(dotSize)
                .alpha(dotAlpha)
                .background(activeColor, CircleShape)
        )
    }
}

@Composable
fun PlatformLabel(name: String) {
    Text(
        text = "Платформа: $name",
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray
    )
}