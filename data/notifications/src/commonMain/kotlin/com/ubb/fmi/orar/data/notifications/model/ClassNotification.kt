package com.ubb.fmi.orar.data.notifications.model

import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency

data class ClassNotification(
    val id: String,
    val className: String,
    val classType: EventType,
    val frequency: Frequency,
    val day: Day,
    val hour: Int,
    val minute: Int,
    val isDismissed: Boolean = false,
)
