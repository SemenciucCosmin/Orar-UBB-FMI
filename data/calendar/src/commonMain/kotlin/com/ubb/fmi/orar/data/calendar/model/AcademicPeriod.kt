package com.ubb.fmi.orar.data.calendar.model

data class AcademicPeriod(
    val activity: AcademicActivity,
    val weeks: List<AcademicWeek>,
    val startDateMillis: Long,
    val endDateMillis: Long,
)
