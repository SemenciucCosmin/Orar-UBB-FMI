package com.ubb.fmi.orar.data.calendar.model

data class AcademicPeriod(
    val activity: AcademicActivity,
    val weeksCount: Int,
    val startDateMillis: Long,
    val endDateMillis: Long,
)