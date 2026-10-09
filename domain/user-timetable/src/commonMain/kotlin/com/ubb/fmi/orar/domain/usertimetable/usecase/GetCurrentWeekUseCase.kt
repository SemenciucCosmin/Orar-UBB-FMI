package com.ubb.fmi.orar.domain.usertimetable.usecase

import Logger
import com.ubb.fmi.orar.data.timetable.model.Week
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.calendar.usecase.GetAcademicYearUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlin.time.Clock

/**
 * Use case for computing the current week type based on the current date and
 * university year on each semester.
 *
 * The week is read straight off the academic year's teaching weeks, which notification
 * scheduling enumerates as well, so the week shown here and the week a notification fires on can
 * never disagree. On days with no teaching (any break or exam session) the upcoming teaching
 * week is shown instead, since the timetable still has to display one of the two week types.
 */
class GetCurrentWeekUseCase(
    private val timetablePreferences: TimetablePreferences,
    private val getAcademicYearUseCase: GetAcademicYearUseCase,
    private val logger: Logger,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Week> {
        return timetablePreferences.getConfiguration().mapLatest { configuration ->
            val currentMillis = Clock.System.now().toEpochMilliseconds()
            val academicYear = when (val year = configuration?.year) {
                null -> getAcademicYearUseCase(currentMillis)
                else -> getAcademicYearUseCase(year)
            }

            val teachingWeek = academicYear.semesters
                .flatMap { it.teachingWeeks }
                .firstOrNull { currentMillis < it.endMillis }

            val week = when {
                teachingWeek == null -> Week.ODD
                teachingWeek.isOdd -> Week.ODD
                else -> Week.EVEN
            }

            logger.d(TAG, "currentMillis: $currentMillis")
            logger.d(TAG, "teachingWeek: $teachingWeek")
            logger.d(TAG, "week: $week")

            return@mapLatest week
        }
    }

    companion object {
        private const val TAG = "GetCurrentWeekUseCase"
    }
}
