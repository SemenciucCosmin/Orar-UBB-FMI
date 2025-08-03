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

//            val rooms = roomsDataSource.getRooms(year, semester).payload
//            val roomsTimetables = rooms?.map { room ->
//                logger.d("TESTMESSAGE", "room: ${room.name}")
//                roomsDataSource.getRoomTimetable(year, semester, room)
//            }

//            val subjects = subjectsDataSource.getSubjects(year, semester).payload
//            val subject = subjects?.first() ?: return@launch
//            val subjectTimetable = subjectsDataSource.getSubjectTimetable(
//                year = year,
//                semester = semester,
//                subject = subject
//            )

//            val teachers = teachersDataSource.getTeachers(year, semester).payload
//            val teacher = teachers?.first() ?: return@launch
//            val teacherTimetable = teachersDataSource.getTeacherTimetable(
//                year = year,
//                semester = semester,
//                teacher = teacher
//            )

            val studyLines = studyLineDataSource.getStudyLines(year, semester).payload
            val ok = studyLines
//            val studyLineTimetable = studyLineDataSource.getStudyLineTimetable(
//                year = year,
//                semester = semester,
//                studyLine = StudyLine.IE1
//            ).payload

//            val executionTime = timetablesRepository.load(year, semester)
//            logger.d("TESTMESSAGE", "executionTime: $executionTime")
        }
    }
}