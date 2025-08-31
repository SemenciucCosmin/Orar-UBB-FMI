package com.ubb.fmi.orar.domain.timetable.model

enum class StudyLevel(
    val id: String,
    val label: String,
    val notation: String,
) {
    LEVEL_1(
        id = "Anul 1",
        label = "Anul 1",
        notation = "1"
    ),
    LEVEL_2(
        id = "Anul 2",
        label = "Anul 2",
        notation = "2"
    ),
    LEVEL_3(
        id = "Anul 3",
        label = "Anul 3",
        notation = "3"
    );

    companion object {
        fun getById(id: String): StudyLevel {
            return entries.firstOrNull { it.id == id } ?: LEVEL_1
        }
    }
}