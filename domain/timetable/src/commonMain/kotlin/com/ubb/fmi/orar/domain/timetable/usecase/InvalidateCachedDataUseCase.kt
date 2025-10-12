package com.ubb.fmi.orar.domain.timetable.usecase

import Logger
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.students.datasource.GroupsDataSource
import com.ubb.fmi.orar.data.students.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.ExperimentalTime

/**
 * Use case for invalidating cached data or current selected configuration in the timetable domain.
 * This use case invalidates cached data for rooms, study lines, subjects, and teachers
 * that are older than two years from the current year.
 *
 * @property timetablePreferences The data store preferences for timetable configuration
 * @property roomsDataSource The data source for room-related operations.
 * @property studyLineDataSource The data source for study line-related operations.
 * @property subjectsDataSource The data source for subject-related operations.
 * @property teachersDataSource The data source for teacher-related operations.
 */
class InvalidateCachedDataUseCase(
    private val timetablePreferences: TimetablePreferences,
    private val eventsDataSource: EventsDataSource,
    private val groupsDataSource: GroupsDataSource,
    private val roomsDataSource: RoomsDataSource,
    private val studyLineDataSource: StudyLinesDataSource,
    private val subjectsDataSource: SubjectsDataSource,
    private val teachersDataSource: TeachersDataSource,
    private val logger: Logger,
) {
    /**
     * Invalidates cached data for current selected configuration
     */
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke() {
        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        configuration?.let {
            logger.d(TAG, "Invalidate data for config: $configuration")
            eventsDataSource.invalidate(configuration.year, configuration.semesterId)
            groupsDataSource.invalidate(configuration.year, configuration.semesterId)
            roomsDataSource.invalidate(configuration.year, configuration.semesterId)
            studyLineDataSource.invalidate(configuration.year, configuration.semesterId)
            subjectsDataSource.invalidate(configuration.year, configuration.semesterId)
            teachersDataSource.invalidate(configuration.year, configuration.semesterId)
            timetablePreferences.refresh()
        }
    }

    companion object {
        private const val TAG = "InvalidateCachedDataUseCase"
    }
}
