package com.example.multiplatformtests.MusicPlayer.Control

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

class AndroidMediaPlayerController(private val context: Context) : MediaPlayerController {

    private val _state = MutableStateFlow(PlaybackState())
    override val state: StateFlow<PlaybackState> = _state.asStateFlow()

    val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var progressJob: Job? = null

    val backgroundExoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    @OptIn(UnstableApi::class)
    override fun prepare(audioPath: String, videoPath: String?) {
        val dataSourceFactory = DefaultDataSource.Factory(context)

        val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(audioPath))))

        val mediaSource = if (videoPath != null && File(videoPath).exists()) {
            val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(videoPath))))
            // Первым — видео (без звука), вторым — аудио: итоговый источник синхронный
            MergingMediaSource(videoSource, audioSource)
        } else {
            audioSource
        }

        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _state.value = _state.value.copy(
                        isReady = true,
                        durationMs = exoPlayer.duration.coerceAtLeast(0)
                    )
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _state.value = _state.value.copy(isPlaying = isPlaying)
                if (isPlaying) startProgressLoop() else progressJob?.cancel()
            }
        })

        if (videoPath != null && File(videoPath).exists()) {
            val bgSource = ProgressiveMediaSource.Factory(DefaultDataSource.Factory(context))
                .createMediaSource(MediaItem.fromUri(Uri.fromFile(File(videoPath))))
            backgroundExoPlayer.setMediaSource(bgSource)
            backgroundExoPlayer.volume = 0f // фон точно без звука
            backgroundExoPlayer.prepare()
        }
    }

    private fun startProgressLoop() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive) {
                _state.value = _state.value.copy(positionMs = exoPlayer.currentPosition)
                delay(200)
            }
        }
    }

    override fun play() {
        exoPlayer.play()
        backgroundExoPlayer.play()
    }
    override fun pause() {
        exoPlayer.pause()
        backgroundExoPlayer.pause()
    }
    override fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
        backgroundExoPlayer.seekTo(positionMs) // держим примерно синхронно
    }
//    override fun release() {
//        progressJob?.cancel()
//        exoPlayer.release()
//    }
    override fun release() {
        exoPlayer.release()
        backgroundExoPlayer.release()
    }
}

// Фабрике нужен Context — берём Application через простой holder
object AppContextHolder {
    lateinit var context: Context
}