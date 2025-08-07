package com.ubb.fmi.orar.data.preferences.model

data class TimetableConfiguration(
    val year: Int,
    val semesterId: String,
    val userTypeId: String,
    val degreeId: String?,
    val subjectId: String?,
    val teacherId: String?
)
