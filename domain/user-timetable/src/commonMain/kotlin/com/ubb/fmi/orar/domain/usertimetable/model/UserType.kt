package com.ubb.fmi.orar.domain.usertimetable.model

/**
 * Represents the type of user in the system.
 * Each user type is identified by a unique ID.
 */
enum class UserType(val id: String) {
    STUDENT(id = "student"),
    TEACHER(id = "teacher");

    companion object {
        fun getById(id: String): UserType {
            return entries.firstOrNull { it.id == id } ?: STUDENT
        }
    }
}