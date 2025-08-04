package com.ubb.fmi.orar.data.timetables.repository

import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TimetablesRepositoryImpl(
    private val roomsDataSource: RoomsDataSource,
    private val studyLineDataSource: StudyLineDataSource,
    private val subjectsDataSource: SubjectsDataSource,
    private val teachersDataSource: TeachersDataSource,
): TimetablesRepository {

    @OptIn(ExperimentalTime::class)
    override suspend fun load(year: Int, semesterId: String): Long {
        return withContext(Dispatchers.Default) {
            val startTime = Clock.System.now().toEpochMilliseconds()

            val asyncRooms = async { roomsDataSource.getRooms(year, semesterId) }
            val asyncStudyLines = async { studyLineDataSource.getStudyLines(year, semesterId) }
            val asyncSubjects = async { subjectsDataSource.getSubjects(year, semesterId) }
            val asyncTeachers = async { teachersDataSource.getTeachers(year, semesterId) }

            val roomsResource = asyncRooms.await()
            val studyLinesResource = asyncStudyLines.await()
            val subjectsResource = asyncSubjects.await()
            val teachersResource = asyncTeachers.await()

            val rooms = roomsResource.payload ?: emptyList()
            val studyLines = studyLinesResource.payload ?: emptyList()
            val subjects = subjectsResource.payload ?: emptyList()
            val teachers = teachersResource.payload ?: emptyList()

            val asyncRoomsTimetables = rooms.map { room ->
                async { roomsDataSource.getRoomTimetable(year, semesterId, room) }
            }

            val asyncSubjectsTimetables = subjects.map { subject ->
                async { subjectsDataSource.getSubjectTimetable(year, semesterId, subject) }
            }

            val asyncTeachersTimetables = teachers.map { teacher ->
                async { teachersDataSource.getTeacherTimetable(year, semesterId, teacher) }
            }

            val asyncStudyLinesTimetablesType = studyLines.map { studyLine ->
                async { studyLineDataSource.getStudyLineTimetable(year, semesterId, studyLine) }
            }

            val roomsTimetablesResource = asyncRoomsTimetables.awaitAll()
            val subjectsTimetablesResource = asyncSubjectsTimetables.awaitAll()
            val teachersTimetablesResource = asyncTeachersTimetables.awaitAll()
            val studyLinesTimetablesResource = asyncStudyLinesTimetablesType.awaitAll()

            val roomsTimetables = roomsTimetablesResource.mapNotNull { it.payload }
            val subjectsTimetables = subjectsTimetablesResource.mapNotNull { it.payload }
            val teachersTimetables = teachersTimetablesResource.mapNotNull { it.payload }
            val studyLinesTimetables = studyLinesTimetablesResource.mapNotNull { it.payload }

            val endTime = Clock.System.now().toEpochMilliseconds()
            return@withContext endTime - startTime
        }
    }
}