package com.ubb.fmi.orar.data.subjects.model

data class SubjectClass(
    val id: String,
    val day: String,
    val startHour: String,
    val endHour: String,
    val frequencyId: String,
    val roomId: String,
    val studyLineId: String,
    val participantId: String,
    val participantName: String,
    val classTypeId: String,
    val teacherId: String,
)