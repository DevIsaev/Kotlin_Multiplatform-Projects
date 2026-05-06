package org.example.project.Demo.Shedule

import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

private val subjects = listOf(
    Triple("Технологии программирования", "361 аудитория", "Лабораторный корпус"),
    Triple("Математический анализ", "214 аудитория", "Главный корпус"),
    Triple("Линейная алгебра", "118 аудитория", "Главный корпус"),
    Triple("Физика", "307 аудитория", "Физический корпус"),
    Triple("История", "402 аудитория", "Гуманитарный корпус"),
    Triple("Английский язык", "215 аудитория", "Главный корпус"),
    Triple("Дискретная математика", "112 аудитория", "Главный корпус"),
    Triple("Базы данных", "461 аудитория", "Лабораторный корпус"),
    Triple("Операционные системы", "360 аудитория", "Лабораторный корпус"),
    Triple("Экономика", "301 аудитория", "Гуманитарный корпус"),
)

private val timeSlots = listOf(
    "08:30" to "10:00",
    "10:15" to "11:45",
    "12:00" to "13:30",
    "14:10" to "15:35",
    "15:45" to "17:10",
)

private val lessonTypes = listOf(
    LessonType.LECTURE,
    LessonType.LECTURE,
    LessonType.PRACTICE,
    LessonType.LAB,
)

// Пул мероприятий для генерации
private val sampleEvents = listOf(
    Event("10:00", "12:00", "Студенческая конференция", "Актовый зал"),
    Event("14:00", "15:30", "Встреча с куратором", "Каб. 305"),
    Event("12:00", "13:00", "День открытых дверей", "Главный корпус"),
    Event("16:00", "17:30", "Спортивные соревнования", "Спортзал"),
    Event("09:00", "10:00", "Общее собрание", "Аудитория 101"),
)

private fun randomLesson(start: String, end: String): Lesson {
    if ((0..4).random() == 0) {
        return Lesson(start, end, null, LessonType.FREE)
    }
    val subj = subjects.random()
    return Lesson(
        startTime = start,
        endTime   = end,
        subject   = subj.first,
        type      = lessonTypes.random(),
        auditory  = subj.second,
        building  = subj.third
    )
}

private fun randomDayLessons(): List<Lesson> {
    val count = (1..4).random()
    return timeSlots
        .shuffled()
        .take(count)
        .sortedBy { it.first }
        .map { (start, end) -> randomLesson(start, end) }
}

// ~25% дней имеют мероприятие
private fun randomEvents(): List<Event> {
    return if ((0..3).random() == 0) listOf(sampleEvents.random())
    else emptyList()
}

fun generateDays(startDate: LocalDate, count: Int): List<DaySchedule> {
    return (0 until count).map { offset ->
        val date = startDate.plus(offset, kotlinx.datetime.DateTimeUnit.DAY)
        val isWeekend = date.dayOfWeek.ordinal == 6
        DaySchedule(
            date    = date,
            lessons = if (isWeekend) emptyList() else randomDayLessons(),
            events  = if (isWeekend) emptyList() else randomEvents() // НОВОЕ
        )
    }
}

val monthSchedule = WeekSchedule(
    groupName = "Группа 210а",
    even = generateDays(LocalDate(2026, 1, 1), 31),
    odd  = emptyList()
)

val weekScheduleV2 = WeekSchedule(
    groupName = "Группа 210а",
    even = generateDays(LocalDate(2026, 1, 5), 7),
    odd  = generateDays(LocalDate(2026, 1, 12), 7)
)