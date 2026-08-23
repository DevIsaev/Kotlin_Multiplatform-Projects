package com.example.infrastructure

import com.example.domain.AudioPlayer
import com.example.domain.models.AudioDomain
import com.example.infrastructure.mappers.toInfrastructureAudio
import com.example.infrastructure.player.IPlayer

class AudioPlayerImpl(private var player: IPlayer): AudioPlayer {
    override fun prepare(audio: AudioDomain) {
        player.prepare(audio.toInfrastructureAudio())
    }

    override fun play() {
        player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.stop()
    }

    override fun release() {
        player.release()
    }

    override fun setOnTrackEndCallback(callback: () -> Unit) {
        player.setOnTrackEndCallback(callback)
    }

}