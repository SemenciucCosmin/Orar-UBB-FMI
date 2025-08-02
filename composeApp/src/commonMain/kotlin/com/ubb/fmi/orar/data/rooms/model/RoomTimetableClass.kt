package com.ubb.fmi.orar.data.rooms.model

data class RoomTimetableClass(
    val day: String,
    val hours: String,
    val frequencyId: String,
    val studyLineId: String,
    val classTypeId: String,
    val subjectId: String,
    val teacherId: String,
)
