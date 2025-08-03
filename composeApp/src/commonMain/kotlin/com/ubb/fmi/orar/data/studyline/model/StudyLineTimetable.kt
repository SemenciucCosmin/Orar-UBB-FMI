package com.ubb.fmi.orar.data.studyline.model

data class StudyLineTimetable(
    val studyLine: StudyLine,
    val groupsTimetables: List<StudyLineGroupTimetable>
)
