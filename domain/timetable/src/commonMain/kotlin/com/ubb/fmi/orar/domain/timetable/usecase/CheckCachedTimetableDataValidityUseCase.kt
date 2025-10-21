package com.ubb.fmi.orar.domain.timetable.usecase

import Logger
import com.ubb.fmi.orar.domain.timetable.model.Semester
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Use case for checking the validity of cached data in the timetable domain.
 * This use case invalidates cached data for rooms, study lines, subjects, and teachers
 * that are older than two years from the current year.
 */
class CheckCachedTimetableDataValidityUseCase(
    private val invalidateCachedDataUseCase: InvalidateCachedDataUseCase,
    private val logger: Logger,
) {
    /**
     * Checks the validity of cached data by invalidating entries for the last two years
     * for each semester.
     *
     * This function retrieves the current date, calculates the year two years ago,
     * and invalidates cached data for all semesters for that year.
     */
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke() {
        val currentInstant = Clock.System.now()
        val currentDate = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
        val currentYear = currentDate.year
        val invalidYear = currentYear - 2

        logger.d(TAG, "invalidYear $invalidYear")
        Semester.entries.forEach { semester ->
            invalidateCachedDataUseCase(invalidYear, semester.id)
        }
    }

    companion object {
        private const val TAG = "CheckCachedDataValidityUseCase"
    }
}
