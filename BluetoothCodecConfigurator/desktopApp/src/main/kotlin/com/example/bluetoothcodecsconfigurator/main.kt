package com.example.bluetoothcodecsconfigurator

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.bluetoothcodecsconfigurator.resources.BluetoothController

fun main() = application {
    val controller = remember { BluetoothController(null) }
    Window(onCloseRequest = ::exitApplication, title = "Bluetooth Codec Configurator") {
        App(controller)
    }
}