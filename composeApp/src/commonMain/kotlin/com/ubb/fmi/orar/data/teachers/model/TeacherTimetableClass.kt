package com.ubb.fmi.orar.data.teachers.model

data class TeacherTimetableClass (
    val day: String,
    val hours: String,
    val frequencyId: String,
    val roomId: String,
    val studyLineId: String,
    val classTypeId: String,
    val subjectId: String,
    val subjectName: String,
)
