package com.ubb.fmi.orar.domain.usertimetable.model

data class Timetable(
    val title: String,
    val subtitle: String,
    val classes: List<TimetableClass>
)
