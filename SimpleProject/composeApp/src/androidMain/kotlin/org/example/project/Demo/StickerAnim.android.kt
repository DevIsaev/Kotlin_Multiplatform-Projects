package org.example.project.Demo

import android.net.Uri
import android.view.SurfaceView
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import java.util.zip.GZIPInputStream
import android.widget.VideoView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File

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

@OptIn(UnstableApi::class)
@Composable
actual fun WebmSticker(
    bytes: ByteArray,
    modifier: Modifier,
    loop: Boolean
) {
    val context = LocalContext.current

    val tempFile = remember(bytes) {
        File(context.cacheDir, "sticker_${bytes.hashCode()}.webm").also { file ->
            if (!file.exists()) file.writeBytes(bytes)
        }
    }

    AndroidView(
        factory = { ctx ->
            VideoView(ctx).apply {
                setVideoURI(Uri.fromFile(tempFile))
                setOnPreparedListener { mediaPlayer ->
                    mediaPlayer.isLooping = loop
                    mediaPlayer.setVolume(0f, 0f)
                    start()
                }
                setOnCompletionListener { mediaPlayer ->
                    if (loop) {
                        mediaPlayer.isLooping = true
                        start()
                    }
                }
            }
        },
        modifier = modifier
    )
}