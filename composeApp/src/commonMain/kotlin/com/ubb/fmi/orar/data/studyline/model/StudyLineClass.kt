package com.ubb.fmi.orar.data.studyline.model

data class StudyLineClass(
    val id: String,
    val groupId: String,
    val day: String,
    val hours: String,
    val frequencyId: String,
    val roomId: String,
    val participantId: String,
    val classTypeId: String,
    val subjectId: String,
    val teacherId: String,
)
