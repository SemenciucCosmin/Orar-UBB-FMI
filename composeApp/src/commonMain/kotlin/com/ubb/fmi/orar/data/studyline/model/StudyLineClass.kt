package com.ubb.fmi.orar.data.studyline.model

data class StudyLineClass(
    val id: String,
    val groupId: String,
    val day: String,
    val startHour: String,
    val endHour: String,
    val frequencyId: String,
    val roomId: String,
    val participantId: String,
    val participantName: String,
    val classTypeId: String,
    val subjectId: String,
    val teacherId: String,
    val isVisible: Boolean,
)
