package com.example.domain

import com.example.domain.models.AudioDomain

interface AudioPlayer {
    fun prepare(audio: AudioDomain)
    fun play(audio: AudioDomain)
    fun pause(audio: AudioDomain)
    fun stop()
    fun release()
    fun setOnTrackEndCallback(callback: ()-> Unit)
}