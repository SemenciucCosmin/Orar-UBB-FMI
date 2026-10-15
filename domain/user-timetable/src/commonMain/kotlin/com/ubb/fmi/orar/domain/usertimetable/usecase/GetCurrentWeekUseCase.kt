package com.ubb.fmi.orar.domain.usertimetable.usecase

import Logger
import com.ubb.fmi.orar.data.timetable.model.Week
import com.ubb.fmi.orar.domain.calendar.usecase.GetAcademicCalendarUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

/**
 * Use case for computing the current week type based on the current date and
 * university year on each semester.
 *
 * The week is read straight off the official academic calendar's teaching weeks, which notification
 * scheduling enumerates as well, so the week shown here and the week a notification fires on can
 * never disagree. On days with no teaching (any break or exam session) the upcoming teaching
 * week is shown instead, since the timetable still has to display one of the two week types.
 */
class GetCurrentWeekUseCase(
    private val getAcademicCalendarUseCase: GetAcademicCalendarUseCase,
    private val logger: Logger,
) {

    operator fun invoke(): Flow<Week> {
        return getAcademicCalendarUseCase().map { academicCalendar ->
            val currentMillis = Clock.System.now().toEpochMilliseconds()
            val teachingWeek = academicCalendar?.semesters.orEmpty()
                .flatMap { it.teachingWeeks }
                .firstOrNull { currentMillis < it.endMillis }

            val week = teachingWeek?.week ?: Week.ODD

            logger.d(TAG, "currentMillis: $currentMillis")
            logger.d(TAG, "teachingWeek: $teachingWeek")
            logger.d(TAG, "week: $week")

            return@map week
        }
    }

    companion object {
        private const val TAG = "GetCurrentWeekUseCase"
    }
}
