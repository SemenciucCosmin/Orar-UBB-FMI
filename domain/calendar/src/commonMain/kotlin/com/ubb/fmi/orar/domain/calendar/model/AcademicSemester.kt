package com.ubb.fmi.orar.domain.calendar.model

/**
 * One semester of an [AcademicYear], described by the weeks it actually teaches in.
 *
 * [startMillis] is the start of teaching week 1 and [endMillis] the (exclusive) end of teaching
 * week 14, both in epoch milliseconds, so the range covers the break week(s) interrupting the
 * semester (winter break for semester 1, Easter break for semester 2) but not the exam/resit
 * sessions that follow it.
 */
data class AcademicSemester(
    val index: Int,
    val startMillis: Long,
    val endMillis: Long,
    val teachingWeeks: List<TeachingWeek>,
) {
    operator fun contains(millis: Long): Boolean = millis in startMillis..<endMillis
}
