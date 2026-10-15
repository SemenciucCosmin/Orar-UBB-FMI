package com.ubb.fmi.orar.domain.calendar.usecase

import com.ubb.fmi.orar.data.calendar.model.AcademicCalendar
import com.ubb.fmi.orar.data.calendar.model.AcademicWeek
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Week
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
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
 * Timetable events are enumerated from the teaching weeks of the configured semester only. That
 * is what confines them to school weeks: the winter, Easter, exam, resit and summer periods simply
 * have no teaching week to derive an occurrence from, and neither does the other semester, whose
 * timetable is a different one. Events are cached per year and semester, so they may only be
 * notified about during the semester they were fetched for.
 *
 * Personal events are enumerated from every calendar week, all year round, with the parity of the
 * matching academic calendar week, so they agree with the week label the timetable shows. Weeks
 * outside teaching are odd.
 *
 * The odd/even alternation of [Frequency.WEEK_1] and [Frequency.WEEK_2] follows the teaching week
 * numbering instead of the calendar, so a break of an odd number of weeks (the Easter one)
 * doesn't silently invert it.
 */
class GetUpcomingEventOccurrencesUseCase(
    private val getAcademicCalendarUseCase: GetAcademicCalendarUseCase,
    private val timetablePreferences: TimetablePreferences,
) {

    /**
     * Returns up to [limit] occurrences of an event of [type] held on [day] at
     * [startHour]:[startMinute] with [frequency], each moved back by [advanceMinutes], keeping only
     * those strictly after [afterMillis].
     *
     * Moving back by [advanceMinutes] happens after the occurrence is placed in its week, so a
     * lead time long enough to cross midnight correctly lands on the previous day.
     */
    @Suppress("LongParameterList")
    suspend operator fun invoke(
        type: EventType,
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

        val academicCalendar = getAcademicCalendarUseCase().firstOrNull()
        val weeks = when (type) {
            EventType.PERSONAL -> getCalendarWeeks(academicCalendar, afterMillis, timeZone)
            else -> getTeachingWeeks(academicCalendar, afterMillis, timeZone)
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
     * Returns the teaching weeks of the configured semester, or of the semester in session at
     * [afterMillis] when the configured one isn't in [academicCalendar], as their Monday paired
     * with whether they are odd weeks.
     */
    private suspend fun getTeachingWeeks(
        academicCalendar: AcademicCalendar?,
        afterMillis: Long,
        timeZone: TimeZone,
    ): Sequence<Pair<LocalDate, Boolean>> {
        val semesterId = timetablePreferences.getConfiguration().firstOrNull()?.semesterId
        val semesters = academicCalendar?.semesters.orEmpty()
        val academicSemester = semesters.firstOrNull {
            it.semester.id == semesterId
        } ?: semesters.firstOrNull { afterMillis in it }

        val teachingWeeks = academicSemester?.teachingWeeks.orEmpty()
        return teachingWeeks.asSequence().map { teachingWeek ->
            val weekStartDate = Instant.fromEpochMilliseconds(teachingWeek.startMillis)
                .toLocalDateTime(timeZone)
                .date

            weekStartDate to (teachingWeek.week == Week.ODD)
        }
    }

    /**
     * Returns every calendar week from the one [afterMillis] falls in onwards, as its Monday paired
     * with whether it is an odd week. An occurrence is never notified after the event itself, so
     * no earlier week can hold an occurrence later than [afterMillis].
     */
    private fun getCalendarWeeks(
        academicCalendar: AcademicCalendar?,
        afterMillis: Long,
        timeZone: TimeZone,
    ): Sequence<Pair<LocalDate, Boolean>> {
        val afterDate = Instant.fromEpochMilliseconds(afterMillis).toLocalDateTime(timeZone).date
        val firstWeekStartDate = afterDate.minus(afterDate.dayOfWeek.ordinal, DateTimeUnit.DAY)
        val academicWeeks = academicCalendar
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
