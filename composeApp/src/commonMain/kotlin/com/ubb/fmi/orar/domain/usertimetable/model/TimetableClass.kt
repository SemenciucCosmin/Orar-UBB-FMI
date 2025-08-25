package com.ubb.fmi.orar.domain.usertimetable.model

data class TimetableClass(
    val id: String,
    val day: String,
    val startHour: String,
    val endHour: String,
    val frequencyId: String,
    val subject: String,
    val classType: ClassType,
    val participant: String,
    val teacher: String,
    val room: String,
)
