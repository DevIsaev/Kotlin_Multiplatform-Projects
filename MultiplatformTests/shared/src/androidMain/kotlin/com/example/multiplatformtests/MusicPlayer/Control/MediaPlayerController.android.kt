package com.example.multiplatformtests.MusicPlayer.Control

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

actual fun createMediaPlayerController(): MediaPlayerController =
    AndroidMediaPlayerController(AppContextHolder.context)

@OptIn(UnstableApi::class)
@Composable
actual fun VideoBackgroundSurface(controller: MediaPlayerController, modifier: Modifier) {
    val androidController = controller as AndroidMediaPlayerController
    Box(modifier = modifier) {
        // СЛОЙ 1 — размытый, увеличенный, заполняет весь экран (обрезка не страшна, т.к. размыт)
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .blur(24.dp), // требует API 31+, см. примечание ниже
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = androidController.backgroundExoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM // обрезка ок, это фон
                    setShutterBackgroundColor(android.graphics.Color.BLACK)
                    // ВАЖНО: blur корректно применяется только к TextureView, не к SurfaceView
                    videoSurfaceView?.let {
                        // PlayerView сам создаёт SurfaceView/TextureView по атрибуту surface_type
                    }
                }
            }
        )

        // Затемнение поверх размытого фона для контраста с текстом/контролами
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.25f)))

        // СЛОЙ 2 — чёткое видео по центру, БЕЗ обрезки
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = androidController.exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT // целиком, без обрезки
                }
            }
        )
    }
}