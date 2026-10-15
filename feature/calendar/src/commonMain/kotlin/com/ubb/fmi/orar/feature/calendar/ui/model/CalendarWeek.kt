package com.ubb.fmi.orar.feature.calendar.ui.model

import com.ubb.fmi.orar.data.timetable.model.Week
import kotlinx.collections.immutable.ImmutableList

/**
 * A row of the calendar, from Monday to Sunday.
 *
 * @param week The parity of the week, odd for weeks outside teaching.
 * @param days The days of the week, including those of the adjacent months.
 */
data class CalendarWeek(
    val week: Week,
    val days: ImmutableList<CalendarDay>,
)
