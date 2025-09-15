package com.ubb.fmi.orar.domain.usertimetable.usecase

import Logger
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.usertimetable.model.Week
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.math.ceil
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Use case for computing the current week type based on the current date and
 * university year on each semester
 */
class GetCurrentWeekUseCase(
    private val timetablePreferences: TimetablePreferences,
    private val logger: Logger,
) {

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
    operator fun invoke(): Flow<Week> {
        return timetablePreferences.getConfiguration().mapLatest { configuration ->
            val currentInstant = Clock.System.now()
            val currentDate = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
            val currentYear = currentDate.year
            val year = configuration?.year ?: currentYear

            val semester1StartingDate = getSemester1StartingDate(year)
            val semester2StartingDate = getSemester2StartingDate(year)
            val referenceStartingDate = when {
                currentDate in semester1StartingDate..<semester2StartingDate -> semester1StartingDate
                else -> semester2StartingDate
            }

            logger.d(TAG, "currentDate: $currentDate")
            logger.d(TAG, "semester1StartingDate: $semester1StartingDate")
            logger.d(TAG, "semester2StartingDate: $semester2StartingDate")
            logger.d(TAG, "referenceStartingDate: $referenceStartingDate")

            val daysDifference = currentDate.toEpochDays() - referenceStartingDate.toEpochDays()
            val weeksDifference = ceil(daysDifference.inc().toDouble() / WEEK_LENGTH).toInt()
            val week = Week.getById(weeksDifference % 2)

            logger.d(TAG, "weeksDifference: $weeksDifference")
            logger.d(TAG, "week: $week")

            return@mapLatest week
        }
    }

    /**
     * Returns the starting date of the first semester of the given [year]
     * Closes Monday to October 1st
     */
    private fun getSemester1StartingDate(year: Int): LocalDate {
        val defaultStartingDate = LocalDate(year, Month.OCTOBER, DEFAULT_STARTING_DAY)
        val dayOfWeek = defaultStartingDate.dayOfWeek

        return when {
            dayOfWeek == DayOfWeek.MONDAY -> defaultStartingDate

            else -> {
                val previousMonday = defaultStartingDate.minus(
                    (dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal + WEEK_LENGTH) % WEEK_LENGTH,
                    DateTimeUnit.DAY
                )

                val nextMonday = defaultStartingDate.plus(
                    (DayOfWeek.MONDAY.ordinal - dayOfWeek.ordinal + WEEK_LENGTH) % WEEK_LENGTH,
                    DateTimeUnit.DAY
                )

                val daysToPrevious = (defaultStartingDate - previousMonday).days
                val daysToNext = (nextMonday - defaultStartingDate).days

                when {
                    daysToPrevious <= daysToNext -> previousMonday
                    else -> nextMonday
                }
            }
        }
    }

    /**
     * Returns the starting date of the second semester of the given [year]
     * 21 weeks after the start of the first semester
     */
    private fun getSemester2StartingDate(year: Int): LocalDate {
        val semester1StartingDate = getSemester1StartingDate(year)
        return semester1StartingDate.plus(SEMESTER_1_WEEKS, DateTimeUnit.WEEK)
    }

    companion object {
        private const val TAG = "GetCurrentWeekUseCase"
        private const val DEFAULT_STARTING_DAY = 1
        private const val WEEK_LENGTH = 7
        private const val SEMESTER_1_WEEKS = 21
    }
}