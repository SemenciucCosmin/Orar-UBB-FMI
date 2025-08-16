package com.ubb.fmi.orar.data.teachers.model

data class TeacherClass (
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
    val subjectId: String,
    val subjectName: String,
)
