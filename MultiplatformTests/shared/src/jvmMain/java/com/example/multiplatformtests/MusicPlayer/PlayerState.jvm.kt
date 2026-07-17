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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.multiplatformtests.MusicPlayer.Sources.ArtworkSquare
import com.example.multiplatformtests.MusicPlayer.Sources.FlippableArtwork
import com.example.multiplatformtests.MusicPlayer.Sources.PlatformLabel
import com.example.multiplatformtests.MusicPlayer.Sources.ProgressBar

actual fun getPlatformName(): String = "Desktop (JVM)"

@Composable
actual fun PlatformMusicPlayer(
    state: PlayerState,
    onPlayPause: () -> Unit,
    onSeek: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2D31))
            .padding(16.dp)
    ) {
        var isFlipped by remember { mutableStateOf(false) }

        FlippableArtwork(
            size = 260.dp,
            isFlipped = isFlipped,
            onClick = { isFlipped = !isFlipped }, // тап по обложке
            front = { ArtworkSquare(size = 260.dp) },
            back = {
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                        Text("Формат: FLAC", color = Color.White, style = MaterialTheme.typography.bodyLarge)
                        Text("Битрейт: 1411 kbps", color = Color.Gray)
                        Text("Частота: 44.1 kHz / 16 bit", color = Color.Gray)
                        Text("Размер: 38.4 MB", color = Color.Gray)
                    }
                }
            }
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(state.title, color = Color.White, style = MaterialTheme.typography.titleMedium)
            Text(state.artist, color = Color.Gray)
            Spacer(Modifier.height(8.dp))
            ProgressBar(state.progress, onSeek, Modifier.fillMaxWidth())
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { /* prev */ }) { Text("⏮") }
                IconButton(onClick = onPlayPause) { Text(if (state.isPlaying) "⏸" else "▶") }
                IconButton(onClick = { /* next */ }) { Text("⏭") }
            }
            PlatformLabel("Desktop (JVM)")
        }
    }
}