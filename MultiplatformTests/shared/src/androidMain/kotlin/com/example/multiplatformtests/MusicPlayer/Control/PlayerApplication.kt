package com.example.multiplatformtests.MusicPlayer.Control

import android.app.Application

class PlayerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextHolder.context = applicationContext
    }
}