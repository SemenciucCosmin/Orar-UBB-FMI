package com.ubb.fmi.orar.data.teachers.model

data class TeacherTimetable(
    val teacher: Teacher,
    val classes: List<TeacherTimetableClass>
)
