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
import kotlinx.datetime.LocalDate
import org.example.project.AppSettings
import org.example.project.Demo.Buttons_and_Elements.SmoothBottomNavBar
import org.example.project.Demo.Buttons_and_Elements.SmoothBottomNavItem
import org.example.project.Demo.Buttons_and_Elements.buttonDemo
import org.example.project.Demo.Map.MapDemo
import org.example.project.Demo.News.demoNews
import org.example.project.Demo.Settings.demoSettings
import org.example.project.Demo.Shedule.ScheduleV1
import org.example.project.Demo.Shedule.ScheduleV2
import org.example.project.Demo.Shedule.WeekSchedule
import org.example.project.Demo.Shedule.demoList
import org.example.project.Demo.Shedule.generateDays
import org.example.project.Screens.HTMLparser.HTMLparser



@Composable
fun DemoScreen(
    darkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit) {

    var useWeekSchedule by rememberSaveable { mutableStateOf(AppSettings.useWeekSchedule) }

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

    val monthSchedule: WeekSchedule by lazy {
        WeekSchedule(
            groupName = "Группа 210а",
            even = generateDays(LocalDate(2026, 1, 1), 31),
            odd  = emptyList()
        )
    }

    val weekSchedule: WeekSchedule by lazy {
        WeekSchedule(
            groupName = "Группа 210а",
            even = generateDays(LocalDate(2026, 1, 5), 7),
            odd  = generateDays(LocalDate(2026, 1, 12), 7)
        )
    }


    var currentRoute by rememberSaveable { mutableStateOf("List") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ОСНОВНОЙ КОНТЕНТ
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
        ) {
            when (currentRoute) {
                "Buttons" -> buttonDemo()
                "Settings" -> demoSettings(
                    darkTheme         = darkTheme,
                    onThemeChange     = onThemeChange,
                    useWeekSchedule   = useWeekSchedule,
                    onScheduleToggle  = { newValue ->
                        useWeekSchedule = newValue
                        AppSettings.useWeekSchedule = newValue   // сохраняем
                    }
                )
                "News" -> demoNews()
                "List" -> if (useWeekSchedule) ScheduleV2(weekSchedule) else ScheduleV1(schedule = monthSchedule)
//                    "Parser" -> HTMLparser()
//                    "Parser" -> ScheduleV2(weekSchedule)
                "Parser" -> MapDemo()
                else -> buttonDemo()
            }
        }

        SmoothBottomNavBar(
            items = smothbottomElements,
            currentRoute = currentRoute,
            onItemSelected = { currentRoute = it }
        )
    }
}