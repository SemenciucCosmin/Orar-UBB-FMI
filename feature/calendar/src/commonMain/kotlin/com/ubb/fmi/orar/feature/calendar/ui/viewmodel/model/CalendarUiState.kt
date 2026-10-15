package com.ubb.fmi.orar.feature.calendar.ui.viewmodel.model

import com.ubb.fmi.orar.feature.calendar.ui.model.CalendarMonth
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * CalendarUiState represents the state of the calendar screen in the application.
 *
 * @param months The months of the academic year.
 * @param initialPage The page of the current month, displayed first.
 */
data class CalendarUiState(
    val months: ImmutableList<CalendarMonth> = persistentListOf(),
    val initialPage: Int = 0,
    val isLoading: Boolean = true,
    val errorStatus: ErrorStatus? = null,
)
