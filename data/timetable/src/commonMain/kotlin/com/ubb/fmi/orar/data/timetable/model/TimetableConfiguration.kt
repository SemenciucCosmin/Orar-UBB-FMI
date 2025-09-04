package com.ubb.fmi.orar.data.timetable.model

/**
 * Data class for user chosen configuration for personal timetable
 * @param [year]: year of study (2024, 2025 ...)
 * @param [semesterId]: semester (1, 2)
 * @param [userTypeId]: student or teacher
 * @param [fieldId]: only for student type, study line field
 * @param [studyLevelId]: only for student type, study line level (Year1, Year2, Year3)
 * @param [degreeId]: only for student type, study line degree (License or Master)
 * @param [groupId]: only for student type, study line groupId
 * @param [teacherId]: only for teacher type, teacher
 */
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
