package com.example.multiplatformtests.MusicPlayer.Control

actual object MediaFileLocator {
    actual fun getTracksDirectory(): String = "C:\\TestFolder"
    actual fun getClipsDirectory(): String = "C:\\TestFolder"
}