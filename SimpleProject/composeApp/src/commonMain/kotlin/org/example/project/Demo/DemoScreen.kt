package org.example.project.Demo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.Screens.HTMLparser.HTMLparser
import org.example.project.resources.themes.DarkColors
import org.example.project.resources.themes.LightColors

@Composable
fun DemoScreen() {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors

    val smothbottomElements = listOf(
        SmoothBottomNavItem(
            title = "Кнопки",
            selectedIcon = SimpleProject.icons.Icons.Home,
            unselectedIcon = SimpleProject.icons.Icons.Home,
            route = "Buttons"
        ),
        SmoothBottomNavItem(
            title = "Расписание",
            selectedIcon = SimpleProject.icons.Icons.Calendar,
            unselectedIcon = SimpleProject.icons.Icons.Calendar,
            route = "List",
        ),
        SmoothBottomNavItem(
            title = "Новости",
            selectedIcon = SimpleProject.icons.Icons.Info,
            unselectedIcon = SimpleProject.icons.Icons.Info,
            route = "News"
        ),
        SmoothBottomNavItem(
            title = "Настройки",
            selectedIcon = SimpleProject.icons.Icons.Settings,
            unselectedIcon = SimpleProject.icons.Icons.Settings,
            route = "Settings"
        ),
        SmoothBottomNavItem(
            title = "Парсер",
            selectedIcon = SimpleProject.icons.Icons.Darhboard,
            unselectedIcon = SimpleProject.icons.Icons.Darhboard,
            route = "Parser"
        )
    )

    var currentRoute by rememberSaveable { mutableStateOf("Buttons") }

    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // ОСНОВНОЙ КОНТЕНТ
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp) //  чтобы контент не залезал под navbar
            ) {
                when (currentRoute) {
                    "Buttons" -> buttonDemo()
                    "Settings" -> demoSettings()
                    "News" -> demoNews()
                    "List" -> demoList()
                    "Parser" -> HTMLparser()
                    else -> buttonDemo()
                }
            }


            SmoothBottomNavBar(
                items = smothbottomElements,
                currentRoute = currentRoute,
                onItemSelected = { currentRoute = it },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
//                    .padding(horizontal = 16.dp, vertical = 12.dp) // отступы от краёв
            )
        }
    }
}