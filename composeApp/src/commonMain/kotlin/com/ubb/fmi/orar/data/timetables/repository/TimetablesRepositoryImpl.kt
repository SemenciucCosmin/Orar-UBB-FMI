package com.ubb.fmi.orar.data.timetables.repository

import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TimetablesRepositoryImpl(
    private val roomsDataSource: RoomsDataSource,
    private val studyLineDataSource: StudyLineDataSource,
    private val subjectsDataSource: SubjectsDataSource,
    private val teachersDataSource: TeachersDataSource,
) : TimetablesRepository {

    @OptIn(ExperimentalTime::class)
    override suspend fun load(year: Int, semesterId: String): Long {
        return withContext(Dispatchers.Default) {
            val startTime = Clock.System.now().toEpochMilliseconds()

            val asyncRoomTimetables = async {
                roomsDataSource.getTimetables(year, semesterId)
            }

            val asyncStudyLineTimetables = async {
                studyLineDataSource.getTimetables(year, semesterId)
            }

            val asyncSubjectTimetables = async {
                subjectsDataSource.getTimetables(year, semesterId)
            }

            val asyncTeachersTimetables = async {
                teachersDataSource.getTimetables(year, semesterId)
            }

            val roomTimetablesResource = asyncRoomTimetables.await()
            val studyLineTimetablesResource = asyncStudyLineTimetables.await()
            val subjectTimetablesResource = asyncSubjectTimetables.await()
            val teacherTimetablesResource = asyncTeachersTimetables.await()

            val roomTimetables = roomTimetablesResource.payload ?: emptyList()
            val studyLineTimetables = studyLineTimetablesResource.payload ?: emptyList()
            val subjectTimetables = subjectTimetablesResource.payload ?: emptyList()
            val teacherTimetables = teacherTimetablesResource.payload ?: emptyList()

            val endTime = Clock.System.now().toEpochMilliseconds()
            return@withContext endTime - startTime
        }
    }
}