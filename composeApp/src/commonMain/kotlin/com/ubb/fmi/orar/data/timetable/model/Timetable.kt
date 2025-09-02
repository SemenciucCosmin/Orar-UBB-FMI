package com.ubb.fmi.orar.data.timetable.model

data class Timetable<Owner : TimetableOwner>(
    val owner: Owner,
    val classes: List<TimetableClass>
)
