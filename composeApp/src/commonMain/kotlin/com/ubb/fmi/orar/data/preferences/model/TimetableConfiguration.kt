package com.ubb.fmi.orar.data.preferences.model

sealed class TimetableConfiguration(
    open val year: Int,
    open val semesterId: String,
    open val userTypeId: String,
) {
    data class Student(
        override val year: Int,
        override val semesterId: String,
        override val userTypeId: String,
        val degreeId: String,
        val subjectId: String,
    ): TimetableConfiguration(
        year = year,
        semesterId = semesterId,
        userTypeId = userTypeId
    )

    data class Teacher(
        override val year: Int,
        override val semesterId: String,
        override val userTypeId: String,
        val teacherId: String
    ): TimetableConfiguration(
        year = year,
        semesterId = semesterId,
        userTypeId = userTypeId
    )
}
