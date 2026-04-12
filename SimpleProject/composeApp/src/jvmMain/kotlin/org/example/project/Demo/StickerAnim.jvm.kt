package org.example.project.Demo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.*
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