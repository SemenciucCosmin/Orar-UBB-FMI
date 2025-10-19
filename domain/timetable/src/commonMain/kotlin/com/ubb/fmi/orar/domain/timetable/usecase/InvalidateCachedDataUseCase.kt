package com.ubb.fmi.orar.domain.timetable.usecase

import Logger
import com.ubb.fmi.orar.data.groups.repository.GroupsRepository
import com.ubb.fmi.orar.data.rooms.repository.RoomsRepository
import com.ubb.fmi.orar.data.studylines.repository.StudyLinesRepository
import com.ubb.fmi.orar.data.subjects.repository.SubjectsRepository
import com.ubb.fmi.orar.data.teachers.repository.TeacherRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import kotlinx.serialization.json.Json.Default.configuration
import kotlin.time.ExperimentalTime

/**
 * Use case for invalidating cached data or current selected configuration in the timetable domain.
 * This use case invalidates cached data for rooms, study lines, subjects, and teachers
 * that are older than two years from the current year.
 */
class InvalidateCachedDataUseCase(
    private val eventsDataSource: EventsDataSource,
    private val groupsRepository: GroupsRepository,
    private val roomRepository: RoomsRepository,
    private val studyLinesRepository: StudyLinesRepository,
    private val subjectsRepository: SubjectsRepository,
    private val teacherRepository: TeacherRepository,
    private val logger: Logger,
) {
    /**
     * Invalidates cached data for current selected configuration
     */
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(year: Int, semesterId: String) {
        logger.d(TAG, "Invalidate data for config: $configuration")
        eventsDataSource.invalidate(year, semesterId)
        groupsRepository.invalidate(year, semesterId)
        roomRepository.invalidate(year, semesterId)
        studyLinesRepository.invalidate(year, semesterId)
        subjectsRepository.invalidate(year, semesterId)
        teacherRepository.invalidate(year, semesterId)
    }

    companion object {
        private const val TAG = "InvalidateCachedDataUseCase"
    }
}
