package org.example.project.resources.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.compose.onPrimaryContainerLight
import com.example.compose.onPrimaryDark
import com.example.compose.onPrimaryLight
import com.example.compose.primaryContainerLight
import org.example.project.resources.colors.Accent
import org.example.project.resources.colors.Dark
import org.example.project.resources.colors.DarkBlue
import org.example.project.resources.colors.DarkOrange
import org.example.project.resources.colors.Error
import org.example.project.resources.colors.Info
import org.example.project.resources.colors.Orange
import org.example.project.resources.colors.Primary
import org.example.project.resources.colors.Warning

@Composable
fun AppThemeDemo(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorsDemo else LightColorsDemo,
        content = content
    )
}



// ── Светлая тема
val LightColorsDemo = lightColorScheme(

    primary            = Color(0xFF1A7A96),
    onPrimary          = Color.White,
    primaryContainer   = Color(0xFFCCEEF7),
    onPrimaryContainer = Color(0xFF003544),

    secondary            = Color(0xFFE68F28),
    onSecondary          = Color.White,
    secondaryContainer   = Color(0xFFFFDFAA),
    onSecondaryContainer = Color(0xFF4A2800),


    background    = Color(0xFFEFF4F7),
    onBackground  = Color(0xFF0D1B2A),

    surface          = Color(0xFFFFFFFF),
    onSurface        = Color(0xFF0D1B2A),

    surfaceVariant   = Color(0xFFDCEDF5),
    onSurfaceVariant = Color(0xFF3D5A6E),


    outline        = Color(0xFFB0C8D4),
    outlineVariant = Color(0xFFD0E4EC),

    error   = Color(0xFFBA1A1A),
    onError = Color.White,
)

// ── Тёмная тема
val DarkColorsDemo = darkColorScheme(

    primary            = Color(0xFF8ECAE6),
    onPrimary          = Color(0xFF003544),
    primaryContainer   = Color(0xFF124171),
    onPrimaryContainer = Color(0xFFCCEEF7),


    secondary            = Color(0xFFE68F28),
    onSecondary          = Color(0xFF16172B),
    secondaryContainer   = Color(0xFF3D2000),
    onSecondaryContainer = Color(0xFFFFB703),


    background    = Color(0xFF0F1B2D),
    onBackground  = Color(0xFFE2EDF5),


    surface          = Color(0xFF124171),
    onSurface        = Color(0xFFE2EDF5),

    surfaceVariant   = Color(0xFF1E3352),
    onSurfaceVariant = Color(0xFF8BA3BF),


    outline        = Color(0xFF2E4A6A),
    outlineVariant = Color(0xFF243650),

    error   = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)