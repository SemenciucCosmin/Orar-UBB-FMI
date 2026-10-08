package com.ubb.fmi.orar.domain.calendar.usecase

import com.ubb.fmi.orar.domain.calendar.model.AcademicSemester
import com.ubb.fmi.orar.domain.calendar.model.AcademicYear
import com.ubb.fmi.orar.domain.calendar.model.TeachingWeek
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/**
 * Builds the full [AcademicYear] teaching calendar for any academic year, derived from UBB's
 * fixed structure rather than from a hardcoded list of dates:
 *
 *  1. the academic year starts on the Monday closest to October 1st;
 *  2. semester 1 has 14 weeks of teaching activities;
 *  3. a 2 week winter break falls around Christmas and New Year, interrupting semester 1;
 *  4. semester 1 is followed by a 3 week exam session;
 *  5. then a 1 week resit session;
 *  6. then a 1 week inter-semester break;
 *  7. semester 2 has 14 weeks of teaching activities;
 *  8. a 1 week Easter break interrupts it, on the week Easter falls in;
 *  9. semester 2 is followed by a 3 week exam session;
 * 10. then a resit session of up to 2 weeks;
 * 11. the summer break then runs until the next academic year starts.
 *
 * Because the winter and Easter breaks are placed inside their semester's teaching span rather
 * than appended to it, both semesters always occupy the same number of calendar weeks no matter
 * which weekday Christmas or Easter lands on, which keeps every downstream date stable.
 *
 * All the date arithmetic happens on calendar dates; every boundary of the resulting calendar is
 * then exposed as epoch milliseconds at midnight in the given [TimeZone].
 */
class GetAcademicYearUseCase {

    /** Builds the calendar of the academic year starting in autumn of [year]. */
    operator fun invoke(
        year: Int,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): AcademicYear {
        val startDate = getAcademicYearStartDate(year)
        val winterBreakStartDate = getBreakStartDate(LocalDate(year, Month.DECEMBER, CHRISTMAS_DAY))
        val semester1 = buildSemester(
            number = FIRST_SEMESTER_INDEX,
            startDate = startDate,
            timeZone = timeZone,
            breakStartDates = List(WINTER_BREAK_WEEKS) { weekIndex ->
                winterBreakStartDate.plus(weekIndex, DateTimeUnit.WEEK)
            },
        )

        val easterBreakStartDate = getBreakStartDate(getOrthodoxEasterDate(year.inc()))
        val semester2 = buildSemester(
            number = SECOND_SEMESTER_INDEX,
            startDate = startDate.plus(SEMESTER_1_TOTAL_WEEKS, DateTimeUnit.WEEK),
            timeZone = timeZone,
            breakStartDates = listOf(easterBreakStartDate),
        )

        return AcademicYear(
            startYear = year,
            startMillis = toEpochMillis(startDate, timeZone),
            endMillis = toEpochMillis(getAcademicYearStartDate(year.inc()), timeZone),
            semesters = listOf(semester1, semester2),
        )
    }

    /** Builds the calendar of the academic year the moment [millis] falls in. */
    operator fun invoke(
        millis: Long,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): AcademicYear {
        val date = Instant.fromEpochMilliseconds(millis).toLocalDateTime(timeZone).date
        val startYear = when {
            date < getAcademicYearStartDate(date.year) -> date.year.dec()
            else -> date.year
        }

        return invoke(startYear, timeZone)
    }

    /**
     * Lays out [TEACHING_WEEKS] numbered teaching weeks starting at [startDate], skipping over
     * every week starting on one of [breakStartDates]. Skipped weeks consume calendar time but
     * no week number, so the odd/even alternation survives a break of any length.
     */
    private fun buildSemester(
        number: Int,
        startDate: LocalDate,
        timeZone: TimeZone,
        breakStartDates: List<LocalDate>,
    ): AcademicSemester {
        val teachingWeeks = mutableListOf<TeachingWeek>()
        var weekStartDate = startDate

        while (teachingWeeks.size < TEACHING_WEEKS) {
            if (weekStartDate !in breakStartDates) {
                teachingWeeks.add(
                    TeachingWeek(
                        index = teachingWeeks.size.inc(),
                        startMillis = toEpochMillis(weekStartDate, timeZone),
                        endMillis = toEpochMillis(weekStartDate.plus(1, DateTimeUnit.WEEK), timeZone),
                    )
                )
            }

            weekStartDate = weekStartDate.plus(1, DateTimeUnit.WEEK)
        }

        return AcademicSemester(
            index = number,
            startMillis = toEpochMillis(startDate, timeZone),
            endMillis = teachingWeeks.last().endMillis,
            teachingWeeks = teachingWeeks,
        )
    }

    /** Returns the epoch milliseconds of [date]'s midnight in [timeZone]. */
    private fun toEpochMillis(date: LocalDate, timeZone: TimeZone): Long {
        return date.atStartOfDayIn(timeZone).toEpochMilliseconds()
    }

    /**
     * Returns the starting date of the academic year beginning in autumn of [year], being the
     * Monday closest to October 1st.
     */
    private fun getAcademicYearStartDate(year: Int): LocalDate {
        val defaultStartDate = LocalDate(year, Month.OCTOBER, ACADEMIC_YEAR_DEFAULT_STARTING_DAY)
        val dayOfWeek = defaultStartDate.dayOfWeek
        if (dayOfWeek == DayOfWeek.MONDAY) return defaultStartDate

        val daysToPreviousMonday = (dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal + DAYS_IN_WEEK) % DAYS_IN_WEEK
        val daysToNextMonday = (DayOfWeek.MONDAY.ordinal - dayOfWeek.ordinal + DAYS_IN_WEEK) % DAYS_IN_WEEK

        return when {
            daysToPreviousMonday <= daysToNextMonday -> defaultStartDate.minus(
                daysToPreviousMonday,
                DateTimeUnit.DAY
            )

            else -> defaultStartDate.plus(daysToNextMonday, DateTimeUnit.DAY)
        }
    }

    /**
     * Returns the Monday of the week [date] falls in, which is where a break centered on that
     * date starts.
     */
    private fun getBreakStartDate(date: LocalDate): LocalDate {
        return date.minus(date.dayOfWeek.ordinal, DateTimeUnit.DAY)
    }

    /**
     * Returns the Gregorian date of Orthodox Easter Sunday in [year], which is the one the
     * Romanian academic calendar schedules its Easter break around.
     *
     * Uses Meeus' Julian algorithm, which yields the date in the Julian calendar, then shifts it
     * by the [JULIAN_TO_GREGORIAN_OFFSET_DAYS] the two calendars currently differ by.
     */
    @Suppress("MagicNumber")
    private fun getOrthodoxEasterDate(year: Int): LocalDate {
        val leapYearCycleIndex = year % 4
        val weekdayCycleIndex = year % 7
        val lunarCycleIndex = year % 19
        val daysToPaschalFullMoon = (19 * lunarCycleIndex + 15) % 30
        val daysToFollowingSunday = (2 * leapYearCycleIndex + 4 * weekdayCycleIndex - daysToPaschalFullMoon + 34) % 7

        // Packs the month and the day into one number: the month is the quotient by 31 and the
        // zero-based day of month is the remainder.
        val packedMonthAndDay = daysToPaschalFullMoon + daysToFollowingSunday + 114

        val julianEasterDate = LocalDate(
            year = year,
            month = Month(packedMonthAndDay / 31),
            day = packedMonthAndDay % 31 + 1,
        )

        return julianEasterDate.plus(JULIAN_TO_GREGORIAN_OFFSET_DAYS, DateTimeUnit.DAY)
    }

    companion object {
        private const val ACADEMIC_YEAR_DEFAULT_STARTING_DAY = 1
        private const val CHRISTMAS_DAY = 25
        private const val DAYS_IN_WEEK = 7

        private const val TEACHING_WEEKS = 14
        private const val WINTER_BREAK_WEEKS = 2
        private const val EXAM_SESSION_WEEKS = 3
        private const val RESIT_SESSION_WEEKS = 1
        private const val INTER_SEMESTER_BREAK_WEEKS = 1

        private const val FIRST_SEMESTER_INDEX = 1
        private const val SECOND_SEMESTER_INDEX = 2

        /**
         * How many calendar weeks separate the start of semester 1 from the start of semester 2:
         * its teaching weeks and winter break, followed by the exam session, the resit session
         * and the inter-semester break.
         */
        private const val SEMESTER_1_TOTAL_WEEKS = TEACHING_WEEKS +
            WINTER_BREAK_WEEKS +
            EXAM_SESSION_WEEKS +
            RESIT_SESSION_WEEKS +
            INTER_SEMESTER_BREAK_WEEKS

        /**
         * The Julian calendar currently trails the Gregorian one by 13 days, and will until
         * 2100.
         */
        private const val JULIAN_TO_GREGORIAN_OFFSET_DAYS = 13
    }
}
