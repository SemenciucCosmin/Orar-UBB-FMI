package com.ubb.fmi.orar.data.notifications.model

import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency

data class EventNotification(
    val id: String,
    val eventName: String,
    val eventType: EventType,
    val frequency: Frequency,
    val day: Day,
    val hour: Int,
    val minute: Int,
)
