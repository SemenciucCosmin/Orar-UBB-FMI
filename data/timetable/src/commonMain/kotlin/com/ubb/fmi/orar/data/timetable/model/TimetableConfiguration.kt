package com.ubb.fmi.orar.data.preferences

data class TimetableConfiguration(
    val year: Int,
    val semesterId: String,
    val userTypeId: String,
    val fieldId: String?,
    val studyLevelId: String?,
    val degreeId: String?,
    val groupId: String?,
    val teacherId: String?
)
