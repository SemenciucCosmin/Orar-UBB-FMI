package com.ubb.fmi.orar.data.timetable.model

/**
 * StudyLine model
 * @param [id]: unique identifier
 * @param [name]: name of the owner
 * @param [configurationId]: configuration id to which this owner belongs to
 * @param [fieldId]: study line field
 * @param [levelId]: study line level (Year1, Year2, Year3)
 * @param [degreeId]: study line degree (License or Master)
 */
data class StudyLine(
    val id: String,
    val name: String,
    val fieldId: String,
    val levelId: String,
    val degreeId: String,
    val configurationId: String,
)