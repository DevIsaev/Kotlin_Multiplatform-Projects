package org.example.project.Demo.Shedule


import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate


@Composable
fun demoList(){
    //val schedule = previewSchedule
    //ScheduleV1(schedule)
    //ScheduleV2(schedule)

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

    ScheduleV1(schedule = monthSchedule)
    //ScheduleV2(schedule = weekSchedule)
}


