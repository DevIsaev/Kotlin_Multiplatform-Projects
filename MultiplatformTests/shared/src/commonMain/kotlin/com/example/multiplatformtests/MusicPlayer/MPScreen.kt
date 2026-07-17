package com.example.multiplatformtests.MusicPlayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MPScreen(){
    var state by remember { mutableStateOf(PlayerState()) }

    PlatformMusicPlayer(
        state = state,
        onPlayPause = { state = state.copy(isPlaying = !state.isPlaying) },
        onSeek = { state = state.copy(progress = it) }
    )
}