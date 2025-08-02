package com.ubb.fmi.orar.data.subjects.model

data class SubjectTimetable(
    val subject: Subject,
    val classes: List<SubjectTimetableClass>
)