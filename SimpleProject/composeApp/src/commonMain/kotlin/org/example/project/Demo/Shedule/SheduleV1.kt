package org.example.project.Demo.Shedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.resources.colors.Blue
import org.example.project.resources.colors.DarkBlue
import org.example.project.resources.colors.scheduleColors


@Composable
fun ScheduleV1(schedule: WeekSchedule) {
    val colors = scheduleColors()
    val days = schedule.even
    var selectedIndex by remember { mutableStateOf(0) }

    val shortDayNames = listOf("ПН","ВТ","СР","ЧТ","ПТ","СБ","ВС")
    val monthNames    = listOf("января","февраля","марта","апреля","мая","июня",
        "июля","августа","сентября","октября","ноября","декабря")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Заголовок группы — на всю ширину без горизонтальных отступов
        GroupHeader(groupName = schedule.groupName)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Горизонтальный выбор дня
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                itemsIndexed(days) { index, day ->
                    val isSelected = index == selectedIndex
                    DayChip(
                        dayShort = shortDayNames[day.date.dayOfWeek.ordinal],
                        dayNumber = day.date.dayOfMonth.toString(),
                        isSelected = isSelected,
                        onClick = { selectedIndex = index }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            val sel = days[selectedIndex]
            val dayFullName = listOf("понедельник","вторник","среда","четверг",
                "пятница","суббота","воскресенье")[sel.date.dayOfWeek.ordinal]

            Text(
                text = "${sel.date.dayOfMonth} ${monthNames[sel.date.monthNumber-1]} " +
                        "${sel.date.year},  $dayFullName",
                color = colors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn {
                if (sel.lessons.isEmpty()) {
                    item { EmptyDayCard() }
                } else {
                    item { LessonsGroup(lessons = sel.lessons) }
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}
// Чип дня для V1
@Composable
private fun DayChip(
    dayShort: String,
    dayNumber: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = scheduleColors()
    val bgColor = if (isSelected) colors.accentOrange else Blue

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(
            text = dayShort,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = dayNumber,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}