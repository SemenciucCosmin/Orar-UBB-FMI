package com.ubb.fmi.orar.data.core.model

enum class UserType(val id: String, val label: String) {
    STUDENT(
        id = "student",
        label = "Student"
    ),
    TEACHER(
        id = "teacher",
        label = "Profesor"
    );
}
