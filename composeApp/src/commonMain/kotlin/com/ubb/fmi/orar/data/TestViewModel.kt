package com.ubb.fmi.orar.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.model.Semester
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.timetables.repository.TimetablesRepository
import com.ubb.fmi.orar.logging.Logger
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TestViewModel(
    private val logger: Logger,
    private val roomsDataSource: RoomsDataSource,
    private val teachersDataSource: TeachersDataSource,
    private val subjectsDataSource: SubjectsDataSource,
    private val studyLineDataSource: StudyLineDataSource,
    private val timetablesRepository: TimetablesRepository,
) : ViewModel() {

    @OptIn(ExperimentalTime::class)
    fun test() {
        viewModelScope.launch {
            val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val year = currentDate.year - 1
            val semester = Semester.FIRST

//            val rooms = roomsDataSource.getRooms(year, semester.id).payload
//            val roomsTimetables = rooms?.map { room ->
//                logger.d("TESTMESSAGE", "room: ${room.name}")
//                roomsDataSource.getRoomTimetable(year, semester.id, room)
//            }

//            val subjects = subjectsDataSource.getSubjects(year, semester.id).payload
//            val subject = subjects?.first() ?: return@launch
//            val subjectTimetable = subjectsDataSource.getSubjectTimetable(
//                year = year,
//                semester = semester.id,
//                subject = subject
//            )

//            val teachers = teachersDataSource.getTeachers(year, semester.id).payload
//            val teacher = teachers?.first() ?: return@launch
//            val teacherTimetable = teachersDataSource.getTeacherTimetable(
//                year = year,
//                semester = semester.id,
//                teacher = teacher
//            )

            val studyLines = studyLineDataSource.getStudyLines(year, semester.id).payload
            val ok = studyLines
//            val studyLineTimetable = studyLineDataSource.getStudyLineTimetable(
//                year = year,
//                semester = semester.id,
//                studyLine = StudyLine.IE1
//            ).payload

//            val executionTime = timetablesRepository.load(year, semester.id)
//            logger.d("TESTMESSAGE", "executionTime: $executionTime")
        }
    }

    @OptIn(ExperimentalTime::class)
    fun testYears() {
        val currentInstant = Clock.System.now()
        val currentDate = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
        val currentYear = currentDate.year
        val previousYear = currentYear - 1

        val years = (previousYear..currentYear).map { year ->
            val nextYear = year.inc()
            "$year-$nextYear"
        }

        logger.d("TESTMESSAGE", "$years")
    }

    companion object {
    }
}