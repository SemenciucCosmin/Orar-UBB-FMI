package com.ubb.fmi.orar.data.calendar.model

import com.ubb.fmi.orar.data.timetable.model.Semester
import com.ubb.fmi.orar.data.timetable.model.StudyLanguage

data class AcademicSemester(
    val semester: Semester,
    val isFinal: Boolean,
    val studyLanguages: List<StudyLanguage>,
    val periods: List<AcademicPeriod>
)
