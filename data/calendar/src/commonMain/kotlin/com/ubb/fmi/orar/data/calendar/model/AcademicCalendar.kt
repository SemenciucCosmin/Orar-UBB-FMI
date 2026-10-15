package com.ubb.fmi.orar.data.calendar.model

data class AcademicCalendar(
    val startYear: Int,
    val endYear: Int,
    val semesters: List<AcademicSemester>,
)
