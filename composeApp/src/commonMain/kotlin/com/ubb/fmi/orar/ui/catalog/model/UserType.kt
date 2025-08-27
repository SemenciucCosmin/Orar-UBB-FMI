package com.ubb.fmi.orar.ui.catalog.model

enum class UserType(val id: String, val label: String) {
    STUDENT(
        id = "student",
        label = "Student"
    ),
    TEACHER(
        id = "teacher",
        label = "Profesor"
    );

    companion object {
        fun getById(id: String): UserType {
            return entries.firstOrNull { it.id == id } ?: STUDENT
        }
    }
}