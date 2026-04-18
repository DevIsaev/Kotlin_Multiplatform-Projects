package org.example.project.resources.colors

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.compose.onPrimaryContainerLight
import com.example.compose.onPrimaryDark
import com.example.compose.onPrimaryLight
import com.example.compose.primaryContainerLight
import com.example.compose.primaryLight

// ── Базовые цвета
val Primary      = Color(0xFF219EBC)
val Accent       = Color(0xFF023047)
val Success      = Color(0xFF06A77D)
val Warning      = Color(0xFFFFB703)
val Error        = Color(0xFFFB8500)
val Info         = Color(0xFF8ECAE6)
val Orange       = Color(0xFFE68F28)
val DarkBlue     = Color(0xFF0F1B2D)
val Blue         = Color(0xFF124171)
val DarkOrange   = Color(0xFFA7671A)
val SelectedBlue = Color(0xFF2F6093)
val Dark         = Color(0xFF16172B)

data class ScheduleColorScheme(
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceLight: Color,
    val accentOrange: Color,
    val accentBlue: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val divider: Color,
)

@Composable
fun scheduleColors() = ScheduleColorScheme(
    background    = MaterialTheme.colorScheme.background,
    onBackground  = MaterialTheme.colorScheme.onBackground,
    surface       = MaterialTheme.colorScheme.surface,
    onSurface=MaterialTheme.colorScheme.onSurface,
    surfaceLight  = MaterialTheme.colorScheme.surfaceVariant,
    accentOrange  = MaterialTheme.colorScheme.secondary,
    accentBlue    = MaterialTheme.colorScheme.primary,
    textPrimary   = MaterialTheme.colorScheme.onSurface,
    textSecondary = MaterialTheme.colorScheme.onSurfaceVariant,
    divider       = MaterialTheme.colorScheme.outlineVariant,
)