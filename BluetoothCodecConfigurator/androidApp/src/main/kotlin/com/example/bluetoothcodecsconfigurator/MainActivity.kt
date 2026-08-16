package com.example.bluetoothcodecsconfigurator

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bluetoothcodecsconfigurator.resources.BluetoothController
import com.example.bluetoothcodecsconfigurator.resources.CompanionDeviceHelper

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val controller = BluetoothController(applicationContext)

        lateinit var cdmHelper: CompanionDeviceHelper
        cdmHelper = CompanionDeviceHelper(applicationContext)

        val associationLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            Log.d("MainActivity", "CDM association result: ${result.resultCode}")
        }

        setContent {
            var permissionsGranted by remember { mutableStateOf(hasBtPermissions()) }

            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { result ->
                permissionsGranted = result.values.all { it }
            }

            LaunchedEffect(Unit) {
                if (!permissionsGranted) {
                    launcher.launch(requiredPermissions())
                }
            }

            MaterialTheme {
                if (permissionsGranted) {
                    App(controller,
                        onRequestDeviceAssociation = { deviceName ->
                            if (!cdmHelper.isAssociated(deviceName)) {
                                cdmHelper.requestAssociation(
                                    deviceName = deviceName,
                                    onIntentSenderReady = { sender ->
                                        associationLauncher.launch(
                                            IntentSenderRequest.Builder(sender).build()
                                        )
                                    },
                                    onFailure = { Log.e("MainActivity", "CDM failed", it) }
                                )
                            }
                        }
                        )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Нужно разрешение на Bluetooth")
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { launcher.launch(requiredPermissions()) }) {
                                Text("Предоставить доступ")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun requiredPermissions(): Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
        } else {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }

    private fun hasBtPermissions(): Boolean =
        requiredPermissions().all {
            checkSelfPermission(it) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
}