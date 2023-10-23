package com.example.orarubb_fmi.domain

data class TimetableClass(
    val day: String,
    val startHour: String,
    val endHour: String,
    val week: Week,
    val place: String,
    val participant: Participant,
    val classType: ClassType,
    val discipline: String,
    val professor: String
)
