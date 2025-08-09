package com.ubb.fmi.orar.data.core.model

enum class StudyYear(
    val id: String,
    val label: String,
    val notation: String,
) {
    YEAR_1(
        id = "Anul 1",
        label = "Anul 1",
        notation = "1"
    ),
    YEAR_2(
        id = "Anul 2",
        label = "Anul 2",
        notation = "2"
    ),
    YEAR_3(
        id = "Anul 3",
        label = "Anul 3",
        notation = "3"
    );

    companion object {
        fun getById(id: String): StudyYear {
            return entries.firstOrNull { it.id == id } ?: YEAR_1
        }
    }
}