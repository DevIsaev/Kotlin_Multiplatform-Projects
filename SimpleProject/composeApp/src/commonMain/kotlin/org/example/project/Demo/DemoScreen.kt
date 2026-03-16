package org.example.project.Demo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.example.project.resources.themes.DarkColors
import org.example.project.resources.themes.LightColors


@Composable
fun DemoScreen() {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors

    val bottomElements = listOf(
        BottomBarItem(
            title = "Кнопки",
            selectedIcon = SimpleProject.icons.Icons.Home,
            unselectedIcon = SimpleProject.icons.Icons.Home,
            route = "Buttons"
        ),
        BottomBarItem(
            title = "Расписание",
            selectedIcon = SimpleProject.icons.Icons.Calendar,
            unselectedIcon = SimpleProject.icons.Icons.Calendar,
            route = "List"
        ),
        BottomBarItem(
            title = "Новости",
            selectedIcon = SimpleProject.icons.Icons.Info,
            unselectedIcon = SimpleProject.icons.Icons.Info,
            route = "News"
        ),
        BottomBarItem(
            title = "Настройки",
            selectedIcon = SimpleProject.icons.Icons.Settings,
            unselectedIcon = SimpleProject.icons.Icons.Settings,
            route = "Settings"
        )
    )
    var currentRoute by rememberSaveable { mutableStateOf("Buttons") }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                DemoBottomBar(
                    items = bottomElements,
                    currentRoute = currentRoute,
                    onItemSelected = { route -> currentRoute = route }
                )
            },
            content = {
                    paddingValues ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    when (currentRoute) {
                        "Buttons" -> buttonDemo()
                        "Settings" -> demoSettings()
                        "News" -> demoNews()
                        "List" -> demoList()
                        else -> buttonDemo()
                    }
            }

    }
        )
}
}