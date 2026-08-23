package com.example.infrastructure.player

import com.example.infrastructure.models.AudioInfrastructure

interface IPlayer {
    fun prepare(audio: AudioInfrastructure)
    fun play()
    fun pause()
    fun stop()
    fun release()
    fun setOnTrackEndCallback(callback: ()-> Unit)
}