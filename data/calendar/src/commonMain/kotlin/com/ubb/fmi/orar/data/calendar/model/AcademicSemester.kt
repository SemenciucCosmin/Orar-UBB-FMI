package com.ubb.fmi.orar.data.calendar.model

import com.ubb.fmi.orar.data.timetable.model.Semester
import com.ubb.fmi.orar.data.timetable.model.StudyLanguage

data class AcademicSemester(
    val semester: Semester,
    val isFinal: Boolean,
    val studyLanguages: List<StudyLanguage>,
    val periods: List<AcademicPeriod>
) {
    /**
     * The weeks of all teaching periods of this semester
     */
    val teachingWeeks: List<AcademicWeek>
        get() = periods
            .filter { it.activity == AcademicActivity.TEACHING_ACTIVITY }
            .flatMap { it.weeks }

    /**
     * Whether [millis] falls between the start of the first and the end of the last teaching week,
     * including the breaks interrupting the semester.
     */
    operator fun contains(millis: Long): Boolean {
        val weeks = teachingWeeks
        val startMillis = weeks.firstOrNull()?.startMillis ?: return false
        val endMillis = weeks.last().endMillis
        return millis in startMillis..<endMillis
    }
}
