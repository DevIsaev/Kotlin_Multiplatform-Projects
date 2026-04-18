package org.example.project.Demo.Shedule

import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

// Пул предметов
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

private fun randomLesson(start: String, end: String): Lesson {
    // ~20% шанс на свободную пару
    if ((0..4).random() == 0) {
        return Lesson(start, end, null, LessonType.FREE)
    }
    val subj = subjects.random()
    return Lesson(
        startTime = start,
        endTime = end,
        subject = subj.first,
        type = lessonTypes.random(),
        auditory = subj.second,
        building = subj.third
    )
}

private fun randomDayLessons(): List<Lesson> {
    // 1-4 пары в день, берём случайные слоты по порядку
    val count = (1..4).random()
    return timeSlots
        .shuffled()
        .take(count)
        .sortedBy { it.first }
        .map { (start, end) -> randomLesson(start, end) }
}

fun generateDays(startDate: LocalDate, count: Int): List<DaySchedule> {
    return (0 until count).map { offset ->
        val date = startDate.plus(offset, kotlinx.datetime.DateTimeUnit.DAY)
        // Воскресенье — выходной (dayOfWeek: 1=Mon..7=Sun)
        val lessons = if (date.dayOfWeek.ordinal == 6) emptyList()
        else randomDayLessons()
        DaySchedule(date = date, lessons = lessons)
    }
}

// ─── V1: расписание на месяц (31 день) ───────────────────────────────────────

val monthSchedule = WeekSchedule(
    groupName = "Группа 210а",
    even = generateDays(LocalDate(2026, 1, 1), 31),
    odd  = emptyList()   // V1 использует только even
)

// ─── V2: чётная и нечётная неделя (по 7 дней) ────────────────────────────────

val weekScheduleV2 = WeekSchedule(
    groupName = "Группа 210а",
    even = generateDays(LocalDate(2026, 1, 5), 7),  // чётная неделя
    odd  = generateDays(LocalDate(2026, 1, 12), 7)  // нечётная неделя
)