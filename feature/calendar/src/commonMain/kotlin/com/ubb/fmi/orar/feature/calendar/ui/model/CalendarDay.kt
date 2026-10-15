package com.ubb.fmi.orar.feature.calendar.ui.model

import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import kotlinx.datetime.LocalDate

/**
 * A day displayed in the calendar.
 *
 * @param date The date of the day.
 * @param colorItem The color item of the academic activity the day is in.
 * @param isToday Whether the day is today.
 */
data class CalendarDay(
    val date: LocalDate,
    val colorItem: ColorItem,
    val isToday: Boolean,
)
