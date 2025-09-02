package com.ubb.fmi.orar.data.timetable.model

data class TimetableClass(
    val id: String,
    val day: String,
    val startHour: String,
    val endHour: String,
    val frequencyId: String,
    val room: String,
    val field: String,
    val participant: String,
    val classType: String,
    val ownerId: String,
    val groupId: String,
    val ownerTypeId: String,
    val subject: String,
    val teacher: String,
    val isVisible: Boolean,
    val configurationId: String,
)
