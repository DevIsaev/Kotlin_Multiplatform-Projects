package com.example.multiplatformtests

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.multiplatformtests.MusicPlayer.MPScreen
import com.example.multiplatformtests.MusicPlayer.Splash.VectorSplash
import com.example.splash.MusicLogoSplash
import org.jetbrains.compose.resources.painterResource



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
            // основной плеер
            MPScreen()
        }
    }
}