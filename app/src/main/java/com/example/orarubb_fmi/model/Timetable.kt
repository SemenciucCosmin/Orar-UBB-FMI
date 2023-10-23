package com.example.orarubb_fmi.model

data class Timetable(
    val group: String,
    val info: TimetableInfo,
    val classes: List<TimetableClass>
)
