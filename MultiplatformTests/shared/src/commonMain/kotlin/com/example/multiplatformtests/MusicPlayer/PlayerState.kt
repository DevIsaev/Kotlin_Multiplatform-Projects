package com.example.multiplatformtests.MusicPlayer

import androidx.compose.runtime.Composable

data class PlayerState(
    val title: String = "Track Name",
    val artist: String = "Artist Name",
    val progress: Float = 0.4f, // 0..1
    val isPlaying: Boolean = false
)

expect fun getPlatformName(): String

@Composable
expect fun PlatformMusicPlayer(
    state: PlayerState,
    onPlayPause: () -> Unit,
    onSeek: (Float) -> Unit
)