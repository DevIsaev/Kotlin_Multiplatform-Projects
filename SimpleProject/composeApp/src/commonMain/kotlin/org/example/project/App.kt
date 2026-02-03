package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.example.project.Screens.AnimationsScreen
import org.example.project.Screens.SimpleScreen
import org.example.project.Screens.listScreen
import org.example.project.UI.AnimatedTopBar
import org.example.project.UI.BottomBarItem
import org.example.project.UI.CustomBottomBar
import org.example.project.UI.CustomSideDrawer
import org.example.project.UI.CustomTopBar
import org.example.project.UI.CustomTopBarWithActions
import org.example.project.UI.DrawerContent
import org.example.project.UI.DrawerContentSample
import org.example.project.UI.FancyWavyBottomBar
import org.example.project.UI.FloatingRoundedBottomBar
import org.example.project.UI.RoundedCornerShapeBottomBar
import org.example.project.UI.WavyBottomBar
import org.example.project.UI.WavyBottomBarItem
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
    val bottomWaveBarItems = listOf(
        WavyBottomBarItem(
            title = "",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "home"
        ),
        WavyBottomBarItem(
            title = "",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            route = "search"
        ),
        WavyBottomBarItem(
            title = "",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = "settings"
        ),
    )
    val bottomBarItems = listOf(
        BottomBarItem(
            title = "Главная",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "home"
        ),
        BottomBarItem(
            title = "Поиск",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            route = "search"
        ),
        BottomBarItem(
            title = "Настройки",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = "settings"
        ),
    )
    var currentRoute by rememberSaveable { mutableStateOf("home") }
    var selectedScreen by remember { mutableStateOf(0) }
    val screens = listOf("", "", "")

    val drawerState = rememberDrawerState()

    MaterialTheme() {

        CustomSideDrawer(
            drawerState = drawerState,
            drawerContent = {
//                DrawerContent(drawerState)
                DrawerContentSample()
            }
        ) {

            Scaffold(
//                topBar = {
//                    CustomTopBarWithActions(
//                        when (selectedScreen) {
//                            0 -> "Кастомный топбар"
//                            1 -> "Список"
//                            2 -> "Анимация"
//                            else -> "Приложение"
//                        },
//                        onBackClick = {
//                            drawerState.open()
//                        },
//                        actionIcon = {
//                            Icon(
//                                imageVector = Icons.Default.Favorite,
//                                contentDescription = "",
//                                tint = MaterialTheme.colorScheme.onSurface
//                            )
//                        }
//                    )

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
//                },
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
//                    NavigationBar {
//                        screens.forEachIndexed { index, screen ->
//                            NavigationBarItem(
//                                icon = {
//                                    Icon(
//                                        when (index) {
//                                            0 -> Icons.Default.Home
//                                            1 -> Icons.Default.Settings
//                                            2 -> Icons.Default.Person
//                                            else -> Icons.Default.Home
//                                        },
//                                        contentDescription = screen
//                                    )
//                                },
//                                label = { Text(screen) },
//                                selected = selectedScreen == index,
//                                onClick = { selectedScreen = index }
//                            )
//                        }
//                    }

//                    CustomBottomBar(
//                        items = bottomBarItems,
//                        currentRoute = currentRoute,
//                        onItemSelected = { route ->
//                            currentRoute = route
//                        }
//                    )

//                    WavyBottomBar(
//                        items = bottomWaveBarItems,
//                        currentRoute = currentRoute,
//                        onItemSelected = { route ->
//                            currentRoute = route
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(110.dp).background(Color.Green),
//                        containerColor=Color.Transparent,
//                        waveAmplitude = 8.dp,
//                    )


//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 20.dp) // Отступ от нижнего края
//                            .padding(horizontal = 20.dp)
//                    ) {
//                        RoundedCornerShapeBottomBar(
//                            items = bottomBarItems,
//                            currentRoute = currentRoute,
//                            onItemSelected = { route ->
//                                currentRoute = route
//                            }
//                        )
//                    }

//                    FloatingRoundedBottomBar(
//                        items = bottomBarItems,
//                        currentRoute = currentRoute,
//                        onItemSelected = { route ->
//                            currentRoute = route
//                        }
//                    )
                },

                content = {paddingValues ->
                    Box(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    ) {
                        when (currentRoute) {
                            "home" -> SimpleScreen()
                            "search" -> listScreen()
                            "settings" -> AnimationsScreen()
                            else -> SimpleScreen()
                        }

                        FloatingRoundedBottomBar(
                            items = bottomBarItems,
                            currentRoute = currentRoute,
                            onItemSelected = { currentRoute = it },
                            modifier = Modifier
                                .align(Alignment.BottomCenter),
                            backgroundColor = Color.Black
                        )
                    }

                }
            )
//            ) { paddingValues ->
//                Box(modifier = Modifier.padding(paddingValues)) {
//                    when (selectedScreen) {
//                        0 -> SimpleScreen()
//                        1 -> listScreen()
//                        2 -> AnimationsScreen()
//                        else -> SimpleScreen()
//                    }
//                }
//                Box(
//                    modifier = Modifier
//                        .padding(paddingValues)
//                        .fillMaxSize()
//                ) {
//                    when (currentRoute) {
//                        "home" -> SimpleScreen()
//                        "search" -> listScreen()
//                        "settings" -> AnimationsScreen()
//                        else -> SimpleScreen()
//                    }
//                }

//                Box(modifier = Modifier.padding(paddingValues)) {
//                    when (currentRoute) {
//                        "home" -> SimpleScreen()
//                        "search" -> listScreen()
//                        "settings" -> AnimationsScreen()
//                        else -> SimpleScreen()
//                    }
//                }
//            }
        }
    }
}