package com.example.testmusicplayercompose.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testmusicplayercompose.R
import com.example.testmusicplayercompose.presentation.components.AppTitle
import com.example.testmusicplayercompose.presentation.components.Pager
import com.example.testmusicplayercompose.ui.presentation.AudioPlayerTheme
import com.example.testmusicplayercompose.ui.presentation.Navy
import com.example.testmusicplayercompose.ui.presentation.Transparent
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.permissionRequired.collect {
                    if (it) {
                        requestPermission()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.toastError.collect {
                it?.getContentNotHandled()?.let{messageID->
                    Toast.makeText(applicationContext,getString(messageID),Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.checkPermissions(isPermissionGranted())

        enableEdgeToEdge()
        setContent {
            var systemUIController = rememberSystemUiController()
            systemUIController.setSystemBarsColor(Transparent)

            AudioPlayerTheme {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background,
                                Navy
                            )
                        )
                    )
                    .padding(top = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppTitle()
                    Pager()
                }
            }
        }

    }

    private fun isPermissionGranted(): Boolean {
        return if(Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            val isGranted = it.value
            viewModel.checkPermissions(isGranted)
            if(!isGranted) {
                Toast.makeText(this, R.string.permission_warning, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun requestPermission() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.READ_MEDIA_AUDIO)
            )
        } else {
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            )
        }

    }
}