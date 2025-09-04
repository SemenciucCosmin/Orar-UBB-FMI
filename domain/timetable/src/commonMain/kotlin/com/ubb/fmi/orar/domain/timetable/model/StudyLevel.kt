package com.ubb.fmi.orar.domain.timetable.model

/**
 * Represents the study levels in the academic program.
 * Each level is identified by a unique ID and notation.
 * @property id The unique identifier for the study level.
 * @property notation The notation used to represent the study level.
 */
enum class StudyLevel(
    val id: String,
    val notation: String,
) {
    LEVEL_1(
        id = "Anul 1",
        notation = "1"
    ),
    LEVEL_2(
        id = "Anul 2",
        notation = "2"
    ),
    LEVEL_3(
        id = "Anul 3",
        notation = "3"
    );

    companion object {
        fun getById(id: String): StudyLevel {
            return entries.firstOrNull { it.id == id } ?: LEVEL_1
        }
    }
}