package org.example.project.Demo.Settings

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.AppSettings.useWeekSchedule

@Composable
fun demoSettings(
    userName         : String  = "Иванов Иван Иванович",
    userInitial      : Char    = 'И',
    darkTheme        : Boolean,
    onThemeChange    : (Boolean) -> Unit,
    useWeekSchedule  : Boolean,
    onScheduleToggle : (Boolean) -> Unit,
    onLogout         : () -> Unit = {},
) {

    var notificationsEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        SettingsTopBar()
        Spacer(Modifier.height(20.dp))
        SettingsUserCard(userName = userName, initial = userInitial)
        Spacer(Modifier.height(8.dp))

        SettingsOptionsCard(
            notificationsEnabled  = notificationsEnabled,
            onNotificationsToggle = { notificationsEnabled = it },
            darkTheme             = darkTheme,
            onDarkThemeToggle     = onThemeChange,
            useWeekSchedule       = useWeekSchedule,
            onScheduleToggle      = onScheduleToggle,
            onLogout              = onLogout,
        )
    }
}