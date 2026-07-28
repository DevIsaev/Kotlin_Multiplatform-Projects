package com.example.multiplatformtests.MusicPlayer.Control

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.media.MediaView
import javafx.scene.paint.Color

actual fun createMediaPlayerController(): MediaPlayerController = DesktopMediaPlayerController()

@Composable
actual fun VideoBackgroundSurface(controller: MediaPlayerController, modifier: Modifier) {
    val desktopController = controller as DesktopMediaPlayerController
    val state by controller.state.collectAsState()
    val jfxPanel = remember { JFXPanel() }

    LaunchedEffect(state.isVideoReady) {
        if (!state.isVideoReady) return@LaunchedEffect
        val vp = desktopController.videoPlayer ?: return@LaunchedEffect
        Platform.runLater {
            val mediaView = MediaView(vp).apply {
                isPreserveRatio = false
                fitWidth = 800.0
                fitHeight = 600.0
            }
            jfxPanel.scene = Scene(Group(mediaView), Color.BLACK)
        }
    }

    SwingPanel(modifier = modifier, factory = { jfxPanel })
}