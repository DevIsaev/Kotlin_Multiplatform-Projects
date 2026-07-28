package com.example.multiplatformtests.MusicPlayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.multiplatformtests.MusicPlayer.Control.PlayerViewModel
import com.example.multiplatformtests.MusicPlayer.Control.VideoBackgroundSurface
import com.example.multiplatformtests.MusicPlayer.Sources.ArtworkSquare
import com.example.multiplatformtests.MusicPlayer.Sources.FlippableArtwork
import com.example.multiplatformtests.MusicPlayer.Sources.PlatformLabel
import com.example.multiplatformtests.MusicPlayer.Sources.ProgressBar

actual fun getPlatformName(): String = "Desktop (JVM)"

@Composable
actual fun PlatformMusicPlayer(viewModel: PlayerViewModel) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF2B2D31))) {
        // Видео занимает верхнюю часть, БЕЗ наложения контролов
        VideoBackgroundSurface(
            controller = viewModel.mediaController,
            modifier = Modifier.fillMaxWidth().weight(1f)
        )

        // Контролы — отдельная область снизу, не перекрывается видео
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1B1B1B))
                .padding(16.dp)
        ) {
            val fraction = if (state.durationMs > 0) {
                state.positionMs.toFloat() / state.durationMs.toFloat()
            } else 0f

            ProgressBar(fraction, { viewModel.onSeek(it) }, Modifier.fillMaxWidth())
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { /* prev */ }) { Text("⏮") }
                IconButton(onClick = { viewModel.togglePlayPause() }) {
                    Text(if (state.isPlaying) "⏸" else "▶")
                }
                IconButton(onClick = { /* next */ }) { Text("⏭") }
            }
            PlatformLabel("Desktop (JVM)")
        }
    }
}