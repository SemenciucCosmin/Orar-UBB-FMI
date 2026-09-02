package com.ubb.fmi.orar.data.notifications.model

import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency

data class EventNotification(
    val id: String,
    val activity: String,
    val type: EventType,
    val location: String,
    val participant: String,
    val frequency: Frequency,
    val day: Day,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
)
