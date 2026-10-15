package com.ubb.fmi.orar.data.timetable.model

/**
 * StudyLine model
 * @param [id]: unique identifier
 * @param [name]: name of the owner
 * @param [configurationId]: configuration id to which this owner belongs to
 * @param [field]: study line field
 * @param [level]: study line level (Year1, Year2, Year3)
 */
data class StudyLine(
    val id: String,
    val name: String,
    val level: StudyLevel,
    val field: Field,
    val configurationId: String,
)