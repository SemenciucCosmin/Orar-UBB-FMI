package com.ubb.fmi.orar.data.rooms.model

data class RoomClass(
    val id: String,
    val day: String,
    val startHour: String,
    val endHour: String,
    val frequencyId: String,
    val studyLineId: String,
    val participantId: String,
    val participantName: String,
    val classTypeId: String,
    val subjectId: String,
    val teacherId: String,
)
