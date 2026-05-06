package org.example.project.Demo.Settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.resources.colors.Orange


// Шапка
@Composable
fun SettingsTopBar() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector        = Icons.Default.Settings,
            contentDescription = null,
            tint               = MaterialTheme.colorScheme.onBackground,
            modifier           = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text       = "Настройки",
            style      = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground
        )
    }
}

// Карточка пользователя
@Composable
fun SettingsUserCard(userName: String, initial: Char) {
    Card(
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier          = Modifier.padding(16.dp)
        ) {
            UserAvatar(initial = initial)
            Spacer(Modifier.width(14.dp))
            Text(
                text       = userName,
                style      = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// Аватар с буквой
@Composable
private fun UserAvatar(initial: Char) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Orange)
    ) {
        Text(
            text       = initial.toString(),
            color      = Color.White,
            fontSize   = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Карточка с опциями
@Composable
fun SettingsOptionsCard(
    notificationsEnabled  : Boolean,
    onNotificationsToggle : (Boolean) -> Unit,
    darkTheme             : Boolean,
    onDarkThemeToggle     : (Boolean) -> Unit,
    useWeekSchedule       : Boolean,
    onScheduleToggle      : (Boolean) -> Unit,
    onLogout              : () -> Unit,
) {
    Card(
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            SettingsToggleRow(
                label           = "Включить уведомления",
                checked         = notificationsEnabled,
                onCheckedChange = onNotificationsToggle
            )
            SettingsDivider()
            SettingsToggleRow(
                label           = "Темная тема",
                checked         = darkTheme,
                onCheckedChange = onDarkThemeToggle
            )
            SettingsDivider()
            SettingsToggleRow(
                label           = "Недельное расписание",
                checked         = useWeekSchedule,
                onCheckedChange = onScheduleToggle
            )
            SettingsDivider()
            SettingsNavigationRow(
                label   = "Покинуть аккаунт",
                onClick = onLogout
            )
        }
    }
}

// Строка с тогглом
@Composable
private fun SettingsToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Text(
            text     = label,
            style    = MaterialTheme.typography.bodyMedium,
            color    = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked         = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor    = Color.White,
                checkedTrackColor    = Orange,
                uncheckedThumbColor  = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor  = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline,
            )
        )
    }
}

// Строка-навигация (стрелка)
@Composable
private fun SettingsNavigationRow(label: String, onClick: () -> Unit) {
    TextButton(
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text     = label,
            style    = MaterialTheme.typography.bodyMedium,
            color    = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector        = Icons.Default.ChevronRight,
            contentDescription = null,
            tint               = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Тонкий разделитель
@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        color     = MaterialTheme.colorScheme.outlineVariant,
        thickness = 0.5.dp,
        modifier  = Modifier.padding(horizontal = 16.dp)
    )
}