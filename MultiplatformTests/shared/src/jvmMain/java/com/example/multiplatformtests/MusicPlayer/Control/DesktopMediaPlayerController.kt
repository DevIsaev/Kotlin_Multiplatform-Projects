package com.example.multiplatformtests.MusicPlayer.Control

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File


class DesktopMediaPlayerController : MediaPlayerController {

    private val _state = MutableStateFlow(PlaybackState())
    override val state: StateFlow<PlaybackState> = _state.asStateFlow()

    var audioPlayer: MediaPlayer? = null
        private set
    var videoPlayer: MediaPlayer? = null
        private set

    override fun prepare(audioPath: String, videoPath: String?) {
        val media = Media(File(audioPath).toURI().toString())
        audioPlayer = MediaPlayer(media).apply {
            setOnReady {
                _state.value = _state.value.copy(
                    isReady = true,
                    durationMs = media.duration.toMillis().toLong()
                )
            }
            currentTimeProperty().addListener { _, _, newTime ->
                _state.value = _state.value.copy(positionMs = newTime.toMillis().toLong())
                videoPlayer?.let { vp ->
                    if (kotlin.math.abs(vp.currentTime.toMillis() - newTime.toMillis()) > 150) {
                        vp.seek(newTime)
                    }
                }
            }
            statusProperty().addListener { _, _, status ->
                _state.value = _state.value.copy(isPlaying = status == MediaPlayer.Status.PLAYING)
            }
        }

        if (videoPath != null && File(videoPath).exists()) {
            val videoMedia = Media(File(videoPath).toURI().toString())
            videoPlayer = MediaPlayer(videoMedia).apply {
                isMute = true
            }
            _state.value = _state.value.copy(isVideoReady = true) // сигнализируем Compose
        }
    }

    override fun play() {
        audioPlayer?.play()
        videoPlayer?.play()
    }

    override fun pause() {
        audioPlayer?.pause()
        videoPlayer?.pause()
    }

    override fun seekTo(positionMs: Long) {
        val d = Duration.millis(positionMs.toDouble())
        audioPlayer?.seek(d)
        videoPlayer?.seek(d)
    }

    override fun release() {
        audioPlayer?.dispose()
        videoPlayer?.dispose()
    }
}