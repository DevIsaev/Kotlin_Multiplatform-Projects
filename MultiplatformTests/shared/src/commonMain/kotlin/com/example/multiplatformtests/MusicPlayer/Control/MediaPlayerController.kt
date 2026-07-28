package com.example.multiplatformtests.MusicPlayer.Control

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.StateFlow

data class PlaybackState(
    val isReady: Boolean = false,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val isVideoReady: Boolean = false
)

interface MediaPlayerController {
    val state: StateFlow<PlaybackState>

    /** audioPath — обязателен, videoPath — null, если клипа нет (тогда просто аудио) */
    fun prepare(audioPath: String, videoPath: String?)
    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun release()
}

// фабрика — на каждой платформе своя actual-реализация
expect fun createMediaPlayerController(): MediaPlayerController

// композабл для рендера видео-клипа на фон — тоже разный на каждой платформе
@Composable
expect fun VideoBackgroundSurface(controller: MediaPlayerController, modifier: Modifier)