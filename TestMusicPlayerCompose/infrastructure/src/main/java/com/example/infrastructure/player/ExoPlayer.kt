package com.example.infrastructure.player

import android.media.browse.MediaBrowser
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.infrastructure.models.AudioInfrastructure
import javax.inject.Inject

class ExoPlayerImpl @Inject constructor(private var exoPlayer: ExoPlayer): IPlayer{
    private var onTrackEndCallback: (()-> Unit)?=null
    override fun prepare(audio: AudioInfrastructure) {
        var mediaItem= MediaItem.fromUri(audio.uri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    override fun play() {
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun stop() {
        exoPlayer.stop()
    }

    override fun release() {
        exoPlayer.release()
    }

    override fun setOnTrackEndCallback(callback: () -> Unit) {
        onTrackEndCallback=callback
    }

    init{
        exoPlayer.addListener(object: Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if(playbackState== Player.STATE_ENDED){
                    onTrackEndCallback!!.invoke()
                }
            }
        })
    }
}