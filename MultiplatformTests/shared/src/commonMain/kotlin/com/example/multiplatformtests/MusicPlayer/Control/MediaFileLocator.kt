package com.example.multiplatformtests.MusicPlayer.Control

// Расположение файлов на устройстве — НЕ в проекте, а в файловой системе пользователя
expect object MediaFileLocator {
    fun getTracksDirectory(): String
    fun getClipsDirectory(): String
}