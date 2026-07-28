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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.multiplatformtests.MusicPlayer.Control.PlayerViewModel
import com.example.multiplatformtests.MusicPlayer.Control.VideoBackgroundSurface
import com.example.multiplatformtests.MusicPlayer.Sources.ArtworkSquare
import com.example.multiplatformtests.MusicPlayer.Sources.FlippableArtwork
import com.example.multiplatformtests.MusicPlayer.Sources.PlatformLabel
import com.example.multiplatformtests.MusicPlayer.Sources.ProgressBar

actual fun getPlatformName(): String = "Android"

@Composable
actual fun PlatformMusicPlayer(viewModel: PlayerViewModel) {
    val context = LocalContext.current
    val uiModeManager = remember {
        context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    }
    val isCarOrTv = uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_CAR ||
            uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION

    val state by viewModel.state.collectAsState()

    if (isCarOrTv) {
        HorizontalCarTvLayout(viewModel, state)
    } else {
        VerticalPhoneLayout(viewModel, state)
    }
}

@Composable
private fun VerticalPhoneLayout(
    viewModel: PlayerViewModel,
    state: com.example.multiplatformtests.MusicPlayer.Control.PlaybackState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        VideoBackgroundSurface(
            controller = viewModel.mediaController,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            val fraction = if (state.durationMs > 0) {
                state.positionMs.toFloat() / state.durationMs.toFloat()
            } else 0f

            ProgressBar(fraction, { viewModel.onSeek(it) }, Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Button(onClick = { /* prev */ }) { Text("⏮") }
                Button(onClick = { viewModel.togglePlayPause() }) {
                    Text(if (state.isPlaying) "⏸" else "▶")
                }
                Button(onClick = { /* next */ }) { Text("⏭") }
            }
            Spacer(Modifier.height(16.dp))
            PlatformLabel("Android (телефон)")
        }
    }
}

@Composable
private fun HorizontalCarTvLayout(
    viewModel: PlayerViewModel,
    state: com.example.multiplatformtests.MusicPlayer.Control.PlaybackState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        VideoBackgroundSurface(
            controller = viewModel.mediaController,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(32.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val fraction = if (state.durationMs > 0) {
                    state.positionMs.toFloat() / state.durationMs.toFloat()
                } else 0f

                ProgressBar(fraction, { viewModel.onSeek(it) }, Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    Button(onClick = { /* prev */ }, modifier = Modifier.size(64.dp)) { Text("⏮") }
                    Button(onClick = { viewModel.togglePlayPause() }, modifier = Modifier.size(64.dp)) {
                        Text(if (state.isPlaying) "⏸" else "▶")
                    }
                    Button(onClick = { /* next */ }, modifier = Modifier.size(64.dp)) { Text("⏭") }
                }
                Spacer(Modifier.height(12.dp))
                PlatformLabel("Android (магнитола/ТВ)")
            }
        }
    }
}