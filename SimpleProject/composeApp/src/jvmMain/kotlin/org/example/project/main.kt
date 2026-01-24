package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        resizable = false,
        onCloseRequest = ::exitApplication,
        title = "SimpleProject",
    ) {
        App()
    }
}