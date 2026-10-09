package com.ubb.fmi.orar.domain.calendar.usecase

import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.domain.calendar.model.AcademicSemester
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.daysUntil
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
 * With a semester, occurrences are enumerated from that semester's teaching weeks only. That is
 * what confines timetable events to school weeks: the winter, Easter, exam, resit and summer
 * periods simply have no teaching week to derive an occurrence from, and neither does the other
 * semester, whose timetable is a different one.
 *
 * Without a semester (personal events), occurrences are enumerated from every calendar week, all
 * year round. Inside a teaching week the week keeps its teaching parity, so personal events agree
 * with the week label the timetable shows; outside teaching, the parity keeps alternating every
 * calendar week from the last teaching week before it.
 *
 * The odd/even alternation of [Frequency.WEEK_1] and [Frequency.WEEK_2] follows the teaching week
 * numbering instead of the calendar, so a break of an odd number of weeks (the Easter one)
 * doesn't silently invert it.
 */
class GetUpcomingEventOccurrencesUseCase(
    private val getAcademicYearUseCase: GetAcademicYearUseCase,
) {

    /**
     * Returns up to [limit] occurrences of an event held on [day] at [startHour]:[startMinute]
     * with [frequency], each moved back by [advanceMinutes], keeping only those strictly after
     * [afterMillis]. Occurrences are bounded to the teaching weeks of [semester], or unbounded
     * when [semester] is `null`.
     *
     * Moving back by [advanceMinutes] happens after the occurrence is placed in its week, so a
     * lead time long enough to cross midnight correctly lands on the previous day.
     */
    @Suppress("LongParameterList")
    operator fun invoke(
        semester: AcademicSemester?,
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

        val weeks = when (semester) {
            null -> getCalendarWeeks(afterMillis, timeZone)
            else -> semester.teachingWeeks.asSequence().map { teachingWeek ->
                val weekStartDate = Instant.fromEpochMilliseconds(teachingWeek.startMillis)
                    .toLocalDateTime(timeZone)
                    .date

                weekStartDate to teachingWeek.isOdd
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
    private fun getCalendarWeeks(afterMillis: Long, timeZone: TimeZone): Sequence<Pair<LocalDate, Boolean>> {
        val afterDate = Instant.fromEpochMilliseconds(afterMillis).toLocalDateTime(timeZone).date
        val firstWeekStartDate = afterDate.minus(afterDate.dayOfWeek.ordinal, DateTimeUnit.DAY)

        return generateSequence(firstWeekStartDate) { weekStartDate ->
            weekStartDate.plus(1, DateTimeUnit.WEEK)
        }.map { weekStartDate ->
            weekStartDate to isOddWeek(weekStartDate, timeZone)
        }
    }

    /**
     * Whether the calendar week starting on [weekStartDate] is odd. A teaching week keeps its own
     * parity; any other week continues alternating from the last teaching week before it, looking
     * back into the previous academic year when needed (e.g. for the summer break).
     */
    private fun isOddWeek(weekStartDate: LocalDate, timeZone: TimeZone): Boolean {
        val weekStartMillis = weekStartDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
        val academicYear = getAcademicYearUseCase(weekStartMillis, timeZone)
        val teachingWeeks = academicYear.semesters.flatMap { it.teachingWeeks }

        teachingWeeks.firstOrNull { weekStartMillis in it }?.let { teachingWeek ->
            return teachingWeek.isOdd
        }

        val lastTeachingWeek = teachingWeeks.lastOrNull { it.endMillis <= weekStartMillis }
            ?: getAcademicYearUseCase(academicYear.startYear.dec(), timeZone)
                .semesters
                .last()
                .teachingWeeks
                .last()

        val lastTeachingWeekStartDate = Instant.fromEpochMilliseconds(lastTeachingWeek.startMillis)
            .toLocalDateTime(timeZone)
            .date

        val weeksSinceLastTeachingWeek = lastTeachingWeekStartDate.daysUntil(weekStartDate) / DAYS_IN_WEEK
        val isParityFlipped = weeksSinceLastTeachingWeek % 2 != 0

        return lastTeachingWeek.isOdd != isParityFlipped
    }

    companion object {
        private const val DAYS_IN_WEEK = 7
    }
}
