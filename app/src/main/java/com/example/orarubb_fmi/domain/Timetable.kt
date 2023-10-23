package com.example.orarubb_fmi.domain

data class Timetable(
    val group: String,
    val info: TimetableInfo,
    val classes: List<TimetableClass>
)
