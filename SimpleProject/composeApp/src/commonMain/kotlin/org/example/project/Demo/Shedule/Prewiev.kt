package org.example.project.Demo.Shedule

import kotlinx.datetime.LocalDate

val previewSchedule = WeekSchedule(
    groupName = "Группа 210а",
    even = listOf(
        DaySchedule(
            date = LocalDate(2026, 1, 1),
            lessons = listOf(
                Lesson("08:30", "10:00", "Технологии программирования",
                    LessonType.LECTURE, "361 аудитория", "Лабораторный корпус"),
                Lesson("08:30", "10:00", null, LessonType.FREE),
                Lesson("08:30", "10:00", "Технологии программирования",
                    LessonType.LECTURE, "361 аудитория", "Лабораторный корпус"),
            )
        ),
        DaySchedule(date = LocalDate(2026, 1, 2), lessons = emptyList()),
        DaySchedule(
            date = LocalDate(2026, 1, 3),
            lessons = listOf(
                Lesson("08:30", "10:00", null, LessonType.FREE),
                Lesson("08:30", "10:00", "Технологии программирования",
                    LessonType.LECTURE, "361 аудитория", "Лабораторный корпус"),
            )
        ),
    ),
    odd = listOf(/* аналогично */)
)