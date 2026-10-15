package com.ubb.fmi.orar.feature.calendar.ui.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDate

/**
 * A page of the calendar.
 *
 * @param firstDate The first day of the month.
 * @param weeks The weeks of the month.
 */
data class CalendarMonth(
    val firstDate: LocalDate,
    val weeks: ImmutableList<CalendarWeek>,
)
