package org.example.project.Demo.Shedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.example.project.resources.colors.DarkBlue
import org.example.project.resources.colors.scheduleColors


@Composable
fun ScheduleV2(schedule: WeekSchedule) {
    val colors = scheduleColors()
    var selectedWeek by remember { mutableStateOf(WeekType.EVEN) }
    val days = if (selectedWeek == WeekType.EVEN) schedule.even else schedule.odd

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Заголовок группы — на всю ширину
        GroupHeader(groupName = schedule.groupName)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            WeekTypeTabs(selected = selectedWeek, onSelect = { selectedWeek = it })

            Spacer(Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(days) { day ->
                    DayHeader(day)
                    if (day.lessons.isEmpty()) {
                        EmptyDayCard()
                    } else {
                        LessonsGroup(lessons = day.lessons)
                    }
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

// Таб-переключатель чётная/нечётная
@Composable
private fun WeekTypeTabs(
    selected: WeekType,
    onSelect: (WeekType) -> Unit
) {
    val colors = scheduleColors()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        WeekTypeTab(
            label = "ЧЁТНАЯ",
            isSelected = selected == WeekType.EVEN,
            onClick = { onSelect(WeekType.EVEN) },
            modifier = Modifier.weight(1f)
        )
        WeekTypeTab(
            label = "НЕЧЁТНАЯ",
            isSelected = selected == WeekType.ODD,
            onClick = { onSelect(WeekType.ODD) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun WeekTypeTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = scheduleColors()
    val bgColor = if (isSelected) colors.accentOrange else DarkBlue
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(bgColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}