package org.example.project.Demo.Shedule


import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate


@Composable
fun demoList(shedule:WeekSchedule){
    //val schedule = previewSchedule
    //ScheduleV1(schedule)
    //ScheduleV2(schedule)



    ScheduleV1(schedule = shedule)
    //ScheduleV2(schedule = weekSchedule)
}


