package org.example.project.Demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.*
import kotlinx.coroutines.delay
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import java.io.File
import java.util.zip.GZIPInputStream

@Composable
actual fun TgsSticker(
    bytes: ByteArray,
    modifier: Modifier,
    loop: Boolean
) {
    val json = remember(bytes) {
        try {
            GZIPInputStream(bytes.inputStream()).bufferedReader().readText()
        } catch (e: Exception) {
            bytes.decodeToString() // на случай если уже JSON
        }
    }

    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(json)
    }

    val animator = rememberLottieAnimatable()

    LaunchedEffect(composition) {
        composition ?: return@LaunchedEffect
        animator.animate(
            composition = composition!!,
            iterations = if (loop) Compottie.IterateForever else 1
        )
    }

    LottieAnimation(
        composition = composition,
        progress = { animator.value },
        modifier = modifier
    )
}

@Composable
actual fun WebmSticker(
    bytes: ByteArray,
    modifier: Modifier,
    loop: Boolean
) {
    // Пишем во временный файл
    val tempFile = remember(bytes) {
        File.createTempFile("sticker_", ".webm").also {
            it.deleteOnExit()
            it.writeBytes(bytes)
        }
    }

    // ImageBitmap для отрисовки кадров
    var currentFrame by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(tempFile) {
        val grabber = FFmpegFrameGrabber(tempFile).apply { start() }
        val converter = Java2DFrameConverter()

        val frameDelay = if (grabber.frameRate > 0)
            (1000L / grabber.frameRate).toLong()
        else
            33L // ~30fps fallback

        try {
            while (true) {
                val frame = grabber.grabImage()
                if (frame == null) {
                    if (loop) {
                        grabber.restart()
                        continue
                    } else break
                }
                val buffered = converter.convert(frame) ?: continue
                currentFrame = buffered.toComposeImageBitmap()
                delay(frameDelay)
            }
        } finally {
            grabber.stop()
        }
    }

    currentFrame?.let { bitmap ->
        Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier = modifier
        )
    } ?: Box(modifier) // пока грузится
}