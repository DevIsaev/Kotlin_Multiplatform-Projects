package com.example.multiplatformtests


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.multiplatformtests.MusicPlayer.MPScreen
import com.example.splash.MusicLogoSplash



@Composable
fun App() {
    MaterialTheme {
        var showSplash by remember { mutableStateOf(true) }

        if (showSplash) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                MusicLogoSplash(onFinished = { showSplash = false })
            }
        } else {
            MPScreen()
        }
    }
}