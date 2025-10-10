package com.ubb.fmi.orar.data.timetable.model

data class Event(
    val id: String,
    val configurationId: String,
    val ownerId: String,
    val day: Day,
    val frequency: Frequency,
    val startHour: Int,
    val endHour: Int,
    val location: Location?,
    val activity: Activity,
    val type: EventType,
    val participant: Participant?,
    val host: Host?,
    val isVisible: Boolean,
)
