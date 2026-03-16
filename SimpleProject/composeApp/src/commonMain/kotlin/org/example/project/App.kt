package org.example.project


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import org.example.project.Demo.DemoScreen
import org.example.project.Screens.MainScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun App() {
    //MainScreen()
      DemoScreen()
}