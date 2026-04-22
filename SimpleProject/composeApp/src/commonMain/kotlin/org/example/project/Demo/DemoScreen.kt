package org.example.project.Demo

import SimpleProject.icons.Icons
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.Demo.Buttons_and_Elements.SmoothBottomNavBar
import org.example.project.Demo.Buttons_and_Elements.SmoothBottomNavItem
import org.example.project.Demo.Buttons_and_Elements.buttonDemo
import org.example.project.Demo.News.demoNews
import org.example.project.Demo.Settings.demoSettings
import org.example.project.Demo.Shedule.demoList
import org.example.project.Screens.HTMLparser.HTMLparser



@Composable
fun DemoScreen(
    darkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit) {

    val smothbottomElements = listOf(
        SmoothBottomNavItem(
            title = "Кнопки",
            selectedIcon = Icons.Home,
            unselectedIcon = Icons.Home,
            route = "Buttons"
        ),
        SmoothBottomNavItem(
            title = "Расписание",
            selectedIcon = Icons.Calendar,
            unselectedIcon = Icons.Calendar,
            route = "List",
        ),
        SmoothBottomNavItem(
            title = "Новости",
            selectedIcon = Icons.Info,
            unselectedIcon = Icons.Info,
            route = "News"
        ),
        SmoothBottomNavItem(
            title = "Настройки",
            selectedIcon = Icons.Settings,
            unselectedIcon = Icons.Settings,
            route = "Settings"
        ),
        SmoothBottomNavItem(
            title = "Парсер",
            selectedIcon = Icons.Darhboard,
            unselectedIcon = Icons.Darhboard,
            route = "Parser"
        )
    )

    var currentRoute by rememberSaveable { mutableStateOf("List") }


        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // ОСНОВНОЙ КОНТЕНТ
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            ) {
                when (currentRoute) {
                    "Buttons" -> buttonDemo()
                    "Settings" -> demoSettings(
                        darkTheme = darkTheme,
                        onThemeChange = onThemeChange
                    )
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
                    .windowInsetsPadding(WindowInsets.navigationBars)
//                    .padding(horizontal = 16.dp, vertical = 12.dp) // отступы от краёв
            )
        }
}