package com.ubb.fmi.orar.feature.form.ui.model

enum class Semester(
    val id: String,
    val label: String,
) {
    FIRST(
        id = "1",
        label = "Semstrul 1"
    ),
    SECOND(
        id = "2",
        label = "Semstrul 2"
    );

    companion object {
        fun getById(id: String): Semester {
            return entries.firstOrNull { it.id == id } ?: FIRST
        }
    }
}