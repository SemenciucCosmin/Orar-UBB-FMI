package com.ubb.fmi.orar.data.studyline.model

import com.ubb.fmi.orar.data.model.StudyLine

data class StudyLineTimetable(
    val studyLine: StudyLine,
    val groupsTimetables: List<StudyLineGroupTimetable>
)
