package com.ubb.fmi.orar.data.subjects.model

data class SubjectTimetableClass(
    val day: String,
    val hours: String,
    val frequencyId: String,
    val roomId: String,
    val studyLineId: String,
    val classTypeId: String,
    val teacherId: String,
)