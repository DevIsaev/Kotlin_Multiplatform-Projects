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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.example.multiplatformtests.MusicPlayer.Sources.ProgressBar

@Composable
fun MPScreen(viewModel: PlayerViewModel = remember { PlayerViewModel() }) {
//    var state by remember { mutableStateOf(PlayerState()) }
//
//    PlatformMusicPlayer(
//        state = state,
//        onPlayPause = { state = state.copy(isPlaying = !state.isPlaying) },
//        onSeek = { state = state.copy(progress = it) }
//    )

//    val state by viewModel.state.collectAsState()
//
//    LaunchedEffect(Unit) { viewModel.loadDemoTrack() }
//    DisposableEffect(Unit) { onDispose { viewModel.release() } }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        // клип растягивается на весь фон
//        VideoBackgroundSurface(
//            controller = viewModel.mediaController,
//            modifier = Modifier.fillMaxSize()
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Black.copy(alpha = 0.35f)) // затемнение поверх клипа
//                .padding(24.dp),
//            verticalArrangement = Arrangement.Bottom
//        ) {
//            val fraction = if (state.durationMs > 0) {
//                state.positionMs.toFloat() / state.durationMs.toFloat()
//            } else 0f
//
//            ProgressBar( // компонент из прошлых ответов
//                progress = fraction,
//                onSeek = { viewModel.onSeek(it) },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(Modifier.height(12.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Button(onClick = { viewModel.togglePlayPause() }) {
//                    Text(if (state.isPlaying) "⏸" else "▶")
//                }
//            }
//        }
//    }


    LaunchedEffect(Unit) { viewModel.loadDemoTrack() }
    DisposableEffect(Unit) { onDispose { viewModel.release() } }

    PlatformMusicPlayer(viewModel)
}