package org.example.project.Demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import java.util.zip.GZIPInputStream

@Composable
actual fun TgsSticker(
    bytes: ByteArray,
    modifier: Modifier,
    loop: Boolean
) {
    // Распаковываем gzip → Lottie JSON
    val json = remember(bytes) {
        try {
            GZIPInputStream(bytes.inputStream()).bufferedReader().readText()
        } catch (e: Exception) {
            null
        }
    } ?: return

    val composition by rememberLottieComposition(
        LottieCompositionSpec.JsonString(json)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = if (loop) LottieConstants.IterateForever else 1
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}