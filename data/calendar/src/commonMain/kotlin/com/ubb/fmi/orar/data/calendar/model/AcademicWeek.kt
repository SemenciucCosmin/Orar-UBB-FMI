package com.ubb.fmi.orar.data.calendar.model

import com.ubb.fmi.orar.data.timetable.model.Week

/**
 * A single week of an [AcademicPeriod], spanning from [startMillis] (Monday at midnight) up to,
 * but excluding, [endMillis] (the following Monday at midnight).
 *
 * Teaching weeks alternate between [Week.ODD] ("sapt. 1") and [Week.EVEN] ("sapt. 2") across all
 * teaching periods of a semester, so a break doesn't shift the rhythm. Any other week is [Week.ODD].
 */
data class AcademicWeek(
    val week: Week,
    val startMillis: Long,
    val endMillis: Long,
) {
    operator fun contains(millis: Long): Boolean = millis in startMillis..<endMillis
}
