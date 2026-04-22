package org.example.project


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import org.example.project.Demo.DemoScreen
import org.example.project.Screens.MainScreen
import org.example.project.resources.themes.AppThemeDemo
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun App() {
      var darkTheme by rememberSaveable { mutableStateOf(false) }

      AppThemeDemo(
            darkTheme = darkTheme
      ) {
            DemoScreen(
                  darkTheme = darkTheme,
                  onThemeChange = { darkTheme = it }
            )
      }
}