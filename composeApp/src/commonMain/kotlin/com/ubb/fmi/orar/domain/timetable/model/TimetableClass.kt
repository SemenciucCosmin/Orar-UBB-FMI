package com.ubb.fmi.orar.domain.timetable.model

data class TimetableClass(
    val id: String,
    val day: String,
    val startHour: String,
    val endHour: String,
    val frequencyId: String,
    val subject: String,
    val classType: ClassType,
    val timetableOwnerType: TimetableOwnerType,
    val participant: String,
    val teacher: String,
    val room: String,
    val isVisible: Boolean
)
