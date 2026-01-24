package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.project.Screens.AnimationsScreen
import org.example.project.Screens.SimpleScreen
import org.example.project.Screens.listScreen
import org.example.project.UI.AnimatedTopBar
import org.example.project.UI.CustomSideDrawer
import org.example.project.UI.CustomTopBar
import org.example.project.UI.CustomTopBarWithActions
import org.example.project.UI.DrawerContent
import org.example.project.UI.rememberDrawerState
import org.example.project.resources.themes.DarkColors
import org.example.project.resources.themes.LightColors
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun App() {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
//    val scrollState = rememberLazyListState()
//    val itemsList = remember {
//        List(50) { index -> "Элемент списка №${index + 1}" }
//    }
    var selectedScreen by remember { mutableStateOf(0) }
    val screens = listOf("Простая страница", "Список", "Анимация")

    val drawerState = rememberDrawerState()

    MaterialTheme() {

        CustomSideDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(drawerState)
            }
        ) {

            Scaffold(
                topBar = {
                    CustomTopBarWithActions(
                        when (selectedScreen) {
                            0 -> "Кастомный топбар"
                            1 -> "Список"
                            2 -> "Анимация"
                            else -> "Приложение"
                        },
                        onBackClick = {
                            drawerState.open()
                        },
                        actionIcon = {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )

//            CustomTopBar(
//                title = "Кастомный тулбар",
//                navigationIcon = {
//                    IconButton(onClick = {}) {
//                        Icon(Icons.Default.List, contentDescription = "")
//                    }
//                },
//            )

//                AnimatedTopBar(
//                    title = "Анимированный TopBar",
//                    scrollState = scrollState
//                )
                },
                bottomBar = {
                    NavigationBar {
                        screens.forEachIndexed { index, screen ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        when (index) {
                                            0 -> Icons.Default.Home
                                            1 -> Icons.Default.Settings
                                            2 -> Icons.Default.Person
                                            else -> Icons.Default.Home
                                        },
                                        contentDescription = screen
                                    )
                                },
                                label = { Text(screen) },
                                selected = selectedScreen == index,
                                onClick = { selectedScreen = index }
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    when (selectedScreen) {
                        0 -> SimpleScreen()
                        1 -> listScreen()
                        2 -> AnimationsScreen()
                        else -> SimpleScreen()
                    }
                }
            }
        }
    }
}