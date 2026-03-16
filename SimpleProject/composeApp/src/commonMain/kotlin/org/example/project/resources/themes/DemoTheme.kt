package org.example.project.resources.themes

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.example.compose.onPrimaryContainerLight
import com.example.compose.onPrimaryDark
import com.example.compose.onPrimaryLight
import com.example.compose.primaryContainerLight
import org.example.project.resources.colors.Primary

val DemoLightColors = lightColorScheme(
    primary = Primary,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryLight
)

val DemoDarkColors = darkColorScheme(
    primary = onPrimaryLight,
    onPrimary = onPrimaryDark,
    primaryContainer = onPrimaryContainerLight,
    onPrimaryContainer = onPrimaryLight
)