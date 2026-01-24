package org.example.project.resources.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.compose.onBackgroundDark
import com.example.compose.onPrimaryContainerLight
import com.example.compose.onPrimaryDark
import com.example.compose.onPrimaryLight
import com.example.compose.primaryContainerLight
import com.example.compose.primaryLight
import com.example.compose.secondaryDark

val LightColors = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryLight
)

val DarkColors = darkColorScheme(
    primary = onPrimaryLight,
    onPrimary = onPrimaryDark,
    primaryContainer = onPrimaryContainerLight,
    onPrimaryContainer = onPrimaryLight
)