package org.example.project.Demo.Shedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun LessonCard(
    lesson: Lesson,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = scheduleColors()

    // Скругление только у первой и последней карточки в группе
    val shape = when {
        isFirst && isLast -> RoundedCornerShape(12.dp)
        isFirst           -> RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp,
            bottomStart = 2.dp, bottomEnd = 2.dp)
        isLast            -> RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp,
            bottomStart = 12.dp, bottomEnd = 12.dp)
        else              -> RoundedCornerShape(2.dp)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface, shape)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Время — всегда, в том числе для свободной пары
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.width(48.dp)
            ) {
                Text(
                    text = lesson.startTime,
                    color = colors.textPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = lesson.endTime,
                    color = colors.textSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.width(8.dp))

            if (lesson.type == LessonType.FREE) {
                // Свободная пара — текст по центру оставшегося места
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Свободная пара",
                        color = colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                // Название + место
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lesson.subject ?: "",
                        color = colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (lesson.auditory != null || lesson.building != null) {
                        Text(
                            text = listOfNotNull(lesson.auditory, lesson.building)
                                .joinToString(" | "),
                            color = colors.textSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
                // Тип занятия
                Text(
                    text = lesson.type.label,
                    color = colors.textSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun LessonsGroup(lessons: List<Lesson>) {
    val colors = scheduleColors()

    // Внешняя обёртка с тенью/обводкой по бокам
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .background(
//                color = colors.onSurface,
//                shape = RoundedCornerShape(35.dp)
//            )
            .padding(horizontal = 3.dp, vertical = 3.dp),  // боковая обводка
        verticalArrangement = Arrangement.spacedBy(2.dp)    // тонкий разделитель между парами
    ) {
        lessons.forEachIndexed { index, lesson ->
            LessonCard(
                lesson = lesson,
                isFirst = index == 0,
                isLast  = index == lessons.lastIndex
            )
        }
    }
}

@Composable
fun GroupHeader(
    groupName: String,
    modifier: Modifier = Modifier
) {
    val colors = scheduleColors()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Blue)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = groupName,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun EmptyDayCard(modifier: Modifier = Modifier) {
    val colors = scheduleColors()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colors.surface, RoundedCornerShape(12.dp))
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Занятия отсутствуют",
            color = colors.textPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun DayHeader(daySchedule: DaySchedule) {
    val colors = scheduleColors()
    val dayNames = listOf("понедельник","вторник","среда",
        "четверг","пятница","суббота","воскресенье")
    val monthNames = listOf("января","февраля","марта","апреля","мая","июня",
        "июля","августа","сентября","октября","ноября","декабря")

    val d = daySchedule.date
    // dayOfWeek: MONDAY=1..SUNDAY=7
    val dayName  = dayNames[d.dayOfWeek.ordinal]
    val monthName = monthNames[d.monthNumber - 1]

    Text(
        text = "${d.dayOfMonth} $monthName ${d.year}, $dayName",
        color = colors.textPrimary,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}