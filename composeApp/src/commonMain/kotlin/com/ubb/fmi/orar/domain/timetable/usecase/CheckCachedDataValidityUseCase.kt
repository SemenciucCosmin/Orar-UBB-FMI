package com.ubb.fmi.orar.domain.timetable.usecase

import com.ubb.fmi.orar.data.groups.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.domain.timetable.model.Semester
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CheckCachedDataValidityUseCase(
    private val roomsDataSource: RoomsDataSource,
    private val studyLineDataSource: StudyLinesDataSource,
    private val subjectsDataSource: SubjectsDataSource,
    private val teachersDataSource: TeachersDataSource,
) {
    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke() {
        val currentInstant = Clock.System.now()
        val currentDate = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
        val currentYear = currentDate.year
        val invalidYear = currentYear - 2

        Semester.entries.forEach { semester ->
            roomsDataSource.invalidate(invalidYear, semester.id)
            studyLineDataSource.invalidate(invalidYear, semester.id)
            subjectsDataSource.invalidate(invalidYear, semester.id)
            teachersDataSource.invalidate(invalidYear, semester.id)
        }
    }
}
