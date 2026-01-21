package org.example.todo


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.onPrimaryContainerLight
import com.example.compose.onPrimaryDark
import com.example.compose.onPrimaryLight
import com.example.compose.primaryContainerLight
import com.example.compose.primaryLight
import com.example.compose.secondaryDark
import com.russhwolf.settings.Settings
import org.example.todo.data.InMemoryStorage
import org.example.todo.domain.ToDoTask
import org.example.todo.screens.HomeScreen
import org.example.todo.screens.TaskScreen

sealed class Screen {
    object Home : Screen()
    data class Task(val task: ToDoTask? = null) : Screen()
}


@Composable
@Preview
fun App() {

    var lightColors = lightColorScheme(
        primary = primaryLight,
        onPrimary = onPrimaryDark,
        primaryContainer = primaryContainerLight,
        onPrimaryContainer = onPrimaryDark
    )
    var darkColors = darkColorScheme(
        primary = onPrimaryLight,
        onPrimary = onPrimaryDark,
        primaryContainer = onPrimaryContainerLight,
        onPrimaryContainer = secondaryDark
    )

    var colors by mutableStateOf(
        if (isSystemInDarkTheme()) darkColors else lightColors
    )
    MaterialTheme(colorScheme = colors) {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
        val storage = remember { InMemoryStorage() }
        val settings = remember { Settings() }
        //val storage: TaskStorage = remember { PersistentStorage(settingsStorage = SettingsStorage(settings)) }

        // Обработчики навигации
        val navigateToHome = { currentScreen = Screen.Home }
        val navigateToTask = { task: ToDoTask? -> currentScreen = Screen.Task(task) }

        when (val screen = currentScreen) {
            is Screen.Home -> HomeScreen(
                storage = storage,
                onTaskClick = { navigateToTask(it) },
                onAddTask = { navigateToTask(null) }
            )

            is Screen.Task -> TaskScreen(
                storage = storage,
                task = screen.task,
                onBack = navigateToHome
            )
        }
    }
}