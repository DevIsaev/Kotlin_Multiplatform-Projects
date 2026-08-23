package com.example.domain

import com.example.domain.models.AudioDomain

interface AudioPlayer {
    fun prepare(audio: AudioDomain)
    fun play()
    fun pause()
    fun stop()
    fun release()
    fun setOnTrackEndCallback(callback: ()-> Unit)
}