package com.ubb.fmi.orar.feature.calendar.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.calendar.model.AcademicCalendar
import com.ubb.fmi.orar.data.calendar.model.AcademicPeriod
import com.ubb.fmi.orar.data.calendar.model.AcademicWeek
import com.ubb.fmi.orar.data.timetable.model.Week
import com.ubb.fmi.orar.domain.calendar.usecase.GetAcademicCalendarUseCase
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.feature.calendar.ui.extensions.colorItem
import com.ubb.fmi.orar.feature.calendar.ui.model.CalendarDay
import com.ubb.fmi.orar.feature.calendar.ui.model.CalendarMonth
import com.ubb.fmi.orar.feature.calendar.ui.model.CalendarWeek
import com.ubb.fmi.orar.feature.calendar.ui.viewmodel.model.CalendarUiState
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * ViewModel for managing the state of the calendar screen.
 */
class CalendarViewModel(
    private val getAcademicCalendarUseCase: GetAcademicCalendarUseCase,
    private val logger: Logger,
) : ViewModel() {

    /**
     * A Job that represents the ongoing data fetch operation for the academic calendar.
     * It can be cancelled to stop the operation if needed.
     */
    private var job: Job

    /**
     * A MutableStateFlow that holds the current UI state of the calendar screen.
     */
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Initializes the ViewModel by starting the data fetch operation for the academic calendar.
     */
    init {
        job = getCalendar()
    }

    /**
     * Fetches the academic calendar and maps it into the months of the calendar screen.
     */
    private fun getCalendar() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }
        getAcademicCalendarUseCase().collectLatest { academicCalendar ->
            logger.d(TAG, "getCalendar academicCalendar: $academicCalendar")
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val months = academicCalendar?.let { getMonths(it, today) }.orEmpty()
            val periods = academicCalendar?.semesters
                ?.flatMap { it.periods }
                ?.sortedBy { it.startDateMillis }
                .orEmpty()

            _uiState.update {
                it.copy(
                    months = months.toImmutableList(),
                    periods = periods.toImmutableList(),
                    initialPage = months.indexOfLast { month -> month.firstDate <= today }.coerceAtLeast(0),
                    isLoading = false,
                    errorStatus = when (academicCalendar) {
                        null -> ErrorStatus.NOT_FOUND
                        else -> null
                    },
                )
            }
        }
    }

    /**
     * Retries the data fetch operation for the academic calendar.
     */
    fun retry() {
        logger.d(TAG, "retry")
        job.cancel()
        job = getCalendar()
    }

    /**
     * Retrieves every month from the start to the end of the [academicCalendar]
     */
    private fun getMonths(
        academicCalendar: AcademicCalendar,
        today: LocalDate
    ): List<CalendarMonth> {
        val periods = academicCalendar.semesters.flatMap { it.periods }
        val teachingWeeks = academicCalendar.semesters.flatMap { it.teachingWeeks }

        val startDateMillis = periods.minOfOrNull { it.startDateMillis } ?: return emptyList()
        val endDateMillis = periods.maxOf { it.endDateMillis }

        val startDate = getDateFromMillis(startDateMillis)
        val endDate = getDateFromMillis(endDateMillis)

        val firstMonthDate = LocalDate(startDate.year, startDate.month, 1)
        val lastMonthDate = LocalDate(endDate.year, endDate.month, 1)
        val monthsCount = firstMonthDate.monthsUntil(lastMonthDate) + 1

        return List(monthsCount) { index ->
            val monthDate = firstMonthDate.plus(index, DateTimeUnit.MONTH)

            CalendarMonth(
                firstDate = monthDate,
                weeks = getWeeks(monthDate, periods, teachingWeeks, today).toImmutableList(),
            )
        }
    }

    /**
     * Retrieves every week, from Monday to Sunday, holding a day of the month starting at [monthDate]
     */
    private fun getWeeks(
        monthDate: LocalDate,
        periods: List<AcademicPeriod>,
        teachingWeeks: List<AcademicWeek>,
        today: LocalDate,
    ): List<CalendarWeek> {
        val nextMonthDate = monthDate.plus(1, DateTimeUnit.MONTH)
        val firstMonday = monthDate.minus(monthDate.dayOfWeek.ordinal, DateTimeUnit.DAY)
        val weeksCount = (firstMonday.daysUntil(nextMonthDate) + DAYS_IN_WEEK - 1) / DAYS_IN_WEEK

        return List(weeksCount) { index ->
            val monday = firstMonday.plus(index, DateTimeUnit.WEEK)
            val mondayMillis = getMillisFromDate(monday)
            val teachingWeek = teachingWeeks.firstOrNull { mondayMillis in it }

            CalendarWeek(
                week = teachingWeek?.week ?: Week.ODD,
                days = getDays(monday, periods, today).toImmutableList(),
            )
        }
    }

    /**
     * Retrieves the days of the week starting at [monday]. Days outside every period fall in the
     * summer holiday before or after the academic year.
     */
    private fun getDays(
        monday: LocalDate,
        periods: List<AcademicPeriod>,
        today: LocalDate,
    ): List<CalendarDay> {
        return List(DAYS_IN_WEEK) { index ->
            val date = monday.plus(index, DateTimeUnit.DAY)
            val dateMillis = getMillisFromDate(date)
            val period = periods.firstOrNull { dateMillis in it.startDateMillis..it.endDateMillis }

            CalendarDay(
                date = date,
                colorItem = period?.activity?.colorItem ?: ColorItem.CALENDAR_HOLIDAY,
                isToday = date == today,
            )
        }
    }

    /**
     * Retrieves the date of [millis]
     */
    private fun getDateFromMillis(millis: Long): LocalDate {
        return Instant
            .fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    }

    /**
     * Retrieves the millis at the start of [date]
     */
    private fun getMillisFromDate(date: LocalDate): Long {
        return date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    companion object {
        private const val TAG = "CalendarViewModel"
        private const val DAYS_IN_WEEK = 7
    }
}
