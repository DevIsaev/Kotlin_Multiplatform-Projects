package org.example.project.Screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.example.project.Screens.Coruntines.CorutinesScreen
import org.example.project.UI.BottomBarItem
import org.example.project.UI.CustomSideDrawer
import org.example.project.UI.DrawerContentSample
import org.example.project.UI.FloatingRoundedBottomBar
import org.example.project.UI.WavyBottomBarItem
import org.example.project.UI.rememberDrawerState
import org.example.project.resources.themes.DarkColors
import org.example.project.resources.themes.LightColors

@Composable
fun MainScreen() {
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
            title = "Список",
            selectedIcon = Icons.Filled.ListAlt,
            unselectedIcon = Icons.Outlined.ListAlt,
            route = "list"
        ),
        BottomBarItem(
            title = "Анимации",
            selectedIcon = Icons.Filled.Animation,
            unselectedIcon = Icons.Outlined.Animation,
            route = "animation"
        ),
        BottomBarItem(
            title = "Коррунтины",
            selectedIcon = Icons.Filled.Edit,
            unselectedIcon = Icons.Outlined.Edit,
            route = "corutines"
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

                content = { paddingValues ->
                    Box(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    ) {
                        when (currentRoute) {
                            "home" -> SimpleScreen()
                            "list" -> listScreen()
                            "animation" -> AnimationsScreen()
                            "corutines" -> CorutinesScreen()
                            else -> SimpleScreen()
                        }

                        FloatingRoundedBottomBar(
                            items = bottomBarItems,
                            currentRoute = currentRoute,
                            onItemSelected = { currentRoute = it },
                            modifier = Modifier
                                .align(Alignment.BottomCenter),
                            selectedColor = Color.White
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