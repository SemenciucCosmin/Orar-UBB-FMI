package com.ubb.fmi.orar.data.studyline.model

data class StudyLineGroupTimetable(
    val groupId: String,
    val classes: List<StudyLineGroupTimetableClass>
)
