package com.ubb.fmi.orar.domain.timetable.usecase

import Logger
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.students.datasource.GroupsDataSource
import com.ubb.fmi.orar.data.students.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.domain.timetable.model.Semester
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Use case for checking the validity of cached data in the timetable domain.
 * This use case invalidates cached data for rooms, study lines, subjects, and teachers
 * that are older than two years from the current year.
 *
 * @property roomsDataSource The data source for room-related operations.
 * @property studyLineDataSource The data source for study line-related operations.
 * @property subjectsDataSource The data source for subject-related operations.
 * @property teachersDataSource The data source for teacher-related operations.
 */
class CheckCachedDataValidityUseCase(
    private val groupsDataSource: GroupsDataSource,
    private val roomsDataSource: RoomsDataSource,
    private val studyLineDataSource: StudyLinesDataSource,
    private val subjectsDataSource: SubjectsDataSource,
    private val teachersDataSource: TeachersDataSource,
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
            groupsDataSource.invalidate(invalidYear, semester.id)
            roomsDataSource.invalidate(invalidYear, semester.id)
            studyLineDataSource.invalidate(invalidYear, semester.id)
            subjectsDataSource.invalidate(invalidYear, semester.id)
            teachersDataSource.invalidate(invalidYear, semester.id)
        }
    }

    companion object {
        private const val TAG = "CheckCachedDataValidityUseCase"
    }
}
