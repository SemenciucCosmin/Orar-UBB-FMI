package com.ubb.fmi.orar.data.studyline.model

data class StudyLineGroupTimetableClass(
    val day: String,
    val hours: String,
    val frequencyId: String,
    val roomId: String,
    val participantId: String,
    val classTypeId: String,
    val subjectId: String,
    val teacherId: String,
)
