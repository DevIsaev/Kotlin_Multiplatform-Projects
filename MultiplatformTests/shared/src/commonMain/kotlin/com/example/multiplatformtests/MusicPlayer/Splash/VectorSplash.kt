package com.example.multiplatformtests.MusicPlayer.Splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Парсим один раз и запоминаем — тяжёлые строки лучше вынести в константы
private val framePath = PathParser().parsePathString(
    "M 0 0 L 280 0 L 280 280 L 0 280 Z"
).toPath()

private val notesPath = PathParser().parsePathString(
    "M 148.363 71 L 148.363 71 C 149.28 71 150.16 71.365 150.809 72.013 ... " // path_2 целиком, скопируй из XML
).toPath()

private val barsPath = PathParser().parsePathString(
    "M 85.46 91 L 85.46 91 C 86.067 91 86.664 91.16 87.19 91.464 ..." // path_1 целиком
).toPath()

private val textPath = PathParser().parsePathString(
    "M 149.79 222 L 146.38 183.5 L 151.605 183.5 ..." // path_3 целиком
).toPath()

@Composable
fun VectorSplash(onFinished: () -> Unit) {
    val notesAlpha = remember { Animatable(0f) }
    val barsAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(250)
        launch { notesAlpha.animateTo(1f, tween(250, easing = LinearEasing)) }

        delay(250) // итого 500мс от старта
        launch { barsAlpha.animateTo(1f, tween(500, easing = LinearEasing)) }
        launch { textAlpha.animateTo(1f, tween(500, easing = LinearEasing)) }

        delay(500) // ждём завершения (500+500)
        onFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(280.dp)) {
            val scaleX = size.width / 280f
            val scaleY = size.height / 280f

            withTransform({ scale(scaleX, scaleY, pivot = Offset.Zero) }) {
                drawPath(barsPath, color = Color(0xFF05A5B5), alpha = barsAlpha.value)
                drawPath(notesPath, color = Color(0xFF9A27B5), alpha = notesAlpha.value)
                drawPath(textPath, color = Color(0xFF05A5B5), alpha = textAlpha.value)
            }
        }
    }
}