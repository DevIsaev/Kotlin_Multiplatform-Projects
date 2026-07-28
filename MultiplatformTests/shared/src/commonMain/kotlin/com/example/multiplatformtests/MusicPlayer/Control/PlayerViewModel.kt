package com.example.multiplatformtests.MusicPlayer.Control

import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel {
    private val controller = createMediaPlayerController()
    val state: StateFlow<PlaybackState> = controller.state

    fun loadDemoTrack() {
        val trackPath = "${MediaFileLocator.getTracksDirectory()}/Melt ice cream.wav"
        val clipPath = "${MediaFileLocator.getClipsDirectory()}/Melt ice cream.mp4"
        controller.prepare(audioPath = trackPath, videoPath = clipPath)
    }

    fun togglePlayPause() {
        if (state.value.isPlaying) controller.pause() else controller.play()
    }

    // fraction 0f..1f — из progress bar
    fun onSeek(fraction: Float) {
        val target = (state.value.durationMs * fraction).toLong()
        controller.seekTo(target)
    }

    fun release() = controller.release()

    // нужен для платформенного VideoBackgroundSurface(controller, ...)
    val mediaController: MediaPlayerController get() = controller
}