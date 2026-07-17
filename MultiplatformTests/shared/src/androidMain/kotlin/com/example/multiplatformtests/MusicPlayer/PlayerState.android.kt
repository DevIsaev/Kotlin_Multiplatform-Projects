package com.example.multiplatformtests.MusicPlayer

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.multiplatformtests.MusicPlayer.Sources.ArtworkSquare
import com.example.multiplatformtests.MusicPlayer.Sources.FlippableArtwork
import com.example.multiplatformtests.MusicPlayer.Sources.PlatformLabel
import com.example.multiplatformtests.MusicPlayer.Sources.ProgressBar

actual fun getPlatformName(): String = "Android"

@Composable
actual fun PlatformMusicPlayer(
    state: PlayerState,
    onPlayPause: () -> Unit,
    onSeek: (Float) -> Unit
) {
    val context = LocalContext.current
    val uiModeManager = remember {
        context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    }
    val isCarOrTv = uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_CAR ||
            uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION

    if (isCarOrTv) {
        HorizontalCarTvLayout(state, onPlayPause, onSeek)
    } else {
        VerticalPhoneLayout(state, onPlayPause, onSeek)
    }
}



@Composable
private fun VerticalPhoneLayout(
    state: PlayerState,
    onPlayPause: () -> Unit,
    onSeek: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
        Spacer(Modifier.height(24.dp))
        Text(state.title, color = Color.White, style = MaterialTheme.typography.titleLarge)
        Text(state.artist, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        ProgressBar(state.progress, onSeek, Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Button(onClick = { /* prev */ }) { Text("⏮") }
            Button(onClick = onPlayPause) { Text(if (state.isPlaying) "⏸" else "▶") }
            Button(onClick = { /* next */ }) { Text("⏭") }
        }
        Spacer(Modifier.height(16.dp))
        PlatformLabel("Android (телефон)")
    }
}

@Composable
private fun HorizontalCarTvLayout(
    state: PlayerState,
    onPlayPause: () -> Unit,
    onSeek: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B))
            .padding(32.dp),
        verticalAlignment = Alignment.CenterVertically
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
        Spacer(Modifier.width(32.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(state.title, color = Color.White, style = MaterialTheme.typography.headlineSmall)
            Text(state.artist, color = Color.LightGray)
            Spacer(Modifier.height(12.dp))
            ProgressBar(state.progress, onSeek, Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Button(onClick = { /* prev */ }, modifier = Modifier.size(64.dp)) { Text("⏮") }
                Button(onClick = onPlayPause, modifier = Modifier.size(64.dp)) {
                    Text(if (state.isPlaying) "⏸" else "▶")
                }
                Button(onClick = { /* next */ }, modifier = Modifier.size(64.dp)) { Text("⏭") }
            }
            Spacer(Modifier.height(12.dp))
            PlatformLabel("Android (магнитола/ТВ)")
        }
    }
}