package org.example.project.Demo.Shedule

import kotlinx.datetime.LocalDate

enum class LessonType(val label: String) {
    LECTURE("Лекция"),
    PRACTICE("Практика"),
    LAB("Лаб. работа"),
    FREE("Свободная пара")
}

enum class WeekType {
    EVEN, ODD
}

data class Lesson(
    val startTime: String,        // "08:30"
    val endTime: String,          // "10:00"
    val subject: String?,         // null если свободная пара
    val type: LessonType,
    val auditory: String? = null, // "361 аудитория"
    val building: String? = null  // "Лабораторный корпус"
)

data class DaySchedule(
    val date: LocalDate,
    val lessons: List<Lesson>,
    val events: List<Event> = emptyList()
)

data class WeekSchedule(
    val groupName: String,
    val even: List<DaySchedule>,
    val odd: List<DaySchedule>
)

// Мероприятие
data class Event(
    val startTime: String,       // "14:10"
    val endTime: String,         // "15:35"
    val title: String,           // "Название мероприятия"
    val location: String? = null // "Где проводиться?"
)