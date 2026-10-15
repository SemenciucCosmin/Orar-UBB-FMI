package com.ubb.fmi.orar.domain.calendar.usecase

import com.ubb.fmi.orar.data.calendar.model.AcademicWeek
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Week
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

/**
 * Returns the upcoming occurrences of a weekly timetable event, as epoch milliseconds in
 * chronological order.
 *
 * With teaching weeks, occurrences are enumerated from those weeks only. That is what confines
 * timetable events to school weeks: the winter, Easter, exam, resit and summer periods simply have
 * no teaching week to derive an occurrence from, and neither does the other semester, whose
 * timetable is a different one.
 *
 * Without teaching weeks (personal events), occurrences are enumerated from every calendar week,
 * all year round, with the parity of the matching academic calendar week, so personal events
 * agree with the week label the timetable shows. Weeks outside teaching are odd.
 *
 * The odd/even alternation of [Frequency.WEEK_1] and [Frequency.WEEK_2] follows the teaching week
 * numbering instead of the calendar, so a break of an odd number of weeks (the Easter one)
 * doesn't silently invert it.
 */
class GetUpcomingEventOccurrencesUseCase(
    private val getAcademicCalendarUseCase: GetAcademicCalendarUseCase,
) {

    /**
     * Returns up to [limit] occurrences of an event held on [day] at [startHour]:[startMinute]
     * with [frequency], each moved back by [advanceMinutes], keeping only those strictly after
     * [afterMillis]. Occurrences are bounded to [teachingWeeks], or unbounded when
     * [teachingWeeks] is `null`.
     *
     * Moving back by [advanceMinutes] happens after the occurrence is placed in its week, so a
     * lead time long enough to cross midnight correctly lands on the previous day.
     */
    @Suppress("LongParameterList")
    suspend operator fun invoke(
        teachingWeeks: List<AcademicWeek>?,
        day: Day,
        frequency: Frequency,
        startHour: Int,
        startMinute: Int,
        advanceMinutes: Int,
        afterMillis: Long,
        limit: Int,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): List<Long> {
        if (limit <= 0) return emptyList()

        val weeks = when (teachingWeeks) {
            null -> getCalendarWeeks(afterMillis, timeZone)
            else -> teachingWeeks.asSequence().map { teachingWeek ->
                val weekStartDate = Instant.fromEpochMilliseconds(teachingWeek.startMillis)
                    .toLocalDateTime(timeZone)
                    .date

                weekStartDate to (teachingWeek.week == Week.ODD)
            }
        }

        return weeks
            .filter { (_, isOdd) ->
                when (frequency) {
                    Frequency.BOTH -> true
                    Frequency.WEEK_1 -> isOdd
                    Frequency.WEEK_2 -> !isOdd
                }
            }
            .map { (weekStartDate, _) -> weekStartDate.plus(day.orderIndex, DateTimeUnit.DAY) }
            .map { eventDate -> eventDate.atTime(startHour, startMinute).toInstant(timeZone) }
            .map { eventInstant -> eventInstant.minus(advanceMinutes.minutes).toEpochMilliseconds() }
            .filter { occurrenceMillis -> occurrenceMillis > afterMillis }
            .take(limit)
            .toList()
    }

    /**
     * Returns every calendar week from the one [afterMillis] falls in onwards, as its Monday paired
     * with whether it is an odd week. An occurrence is never notified after the event itself, so
     * no earlier week can hold an occurrence later than [afterMillis].
     */
    private suspend fun getCalendarWeeks(
        afterMillis: Long,
        timeZone: TimeZone,
    ): Sequence<Pair<LocalDate, Boolean>> {
        val afterDate = Instant.fromEpochMilliseconds(afterMillis).toLocalDateTime(timeZone).date
        val firstWeekStartDate = afterDate.minus(afterDate.dayOfWeek.ordinal, DateTimeUnit.DAY)
        val academicWeeks = getAcademicCalendarUseCase().firstOrNull()
            ?.semesters
            ?.flatMap { semester -> semester.periods.flatMap { it.weeks } }
            .orEmpty()

        return generateSequence(firstWeekStartDate) { weekStartDate ->
            weekStartDate.plus(1, DateTimeUnit.WEEK)
        }.map { weekStartDate ->
            weekStartDate to isOddWeek(weekStartDate, academicWeeks, timeZone)
        }
    }

    /**
     * Whether the calendar week starting on [weekStartDate] is odd: the parity of the academic
     * calendar week it matches, or odd outside the academic calendar.
     */
    private fun isOddWeek(
        weekStartDate: LocalDate,
        academicWeeks: List<AcademicWeek>,
        timeZone: TimeZone,
    ): Boolean {
        val weekStartMillis = weekStartDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
        val academicWeek = academicWeeks.firstOrNull { weekStartMillis in it }
        return academicWeek?.week != Week.EVEN
    }
}
