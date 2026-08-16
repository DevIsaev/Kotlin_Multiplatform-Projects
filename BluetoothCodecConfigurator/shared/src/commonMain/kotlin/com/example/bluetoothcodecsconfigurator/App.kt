package com.example.bluetoothcodecsconfigurator

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.bluetoothcodecsconfigurator.resources.BluetoothCodecScreen
import com.example.bluetoothcodecsconfigurator.resources.BluetoothController

@Composable
fun App(controller: BluetoothController, onRequestDeviceAssociation: (String) -> Unit = {}) {
    MaterialTheme {
        BluetoothCodecScreen(
            controller = controller
        )
    }
}