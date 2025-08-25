package com.ubb.fmi.orar.domain.studylines.usecase

import com.ubb.fmi.orar.domain.usertimetable.model.ClassType
import com.ubb.fmi.orar.domain.usertimetable.model.StudyYear
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.teachers.model.TeacherTitle
import com.ubb.fmi.orar.domain.extensions.COMMA
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.usertimetable.model.Timetable
import com.ubb.fmi.orar.domain.usertimetable.model.TimetableClass
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlin.String

class GetStudyLineTimetableUseCase(
    private val studyLineDataSource: StudyLineDataSource,
    private val roomsDataSource: RoomsDataSource,
    private val subjectsDataSource: SubjectsDataSource,
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences
) {

    suspend operator fun invoke(
        studyLineId: String,
        studyGroupId: String,
    ): Resource<Timetable> = coroutineScope {
        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        if (configuration == null) return@coroutineScope Resource(null, Status.Error)

        val timetableAsync = async {
            studyLineDataSource.getTimetable(
                year = configuration.year,
                semesterId = configuration.semesterId,
                studyLineId = studyLineId,
            )
        }

        val roomsAsync = async {
            roomsDataSource.getRooms(
                year = configuration.year,
                semesterId = configuration.semesterId,
            )
        }

        val subjectsAsync = async {
            subjectsDataSource.getSubjects(
                year = configuration.year,
                semesterId = configuration.semesterId,
            )
        }

        val teachersAsync = async {
            teachersDataSource.getTeachers(
                year = configuration.year,
                semesterId = configuration.semesterId,
            )
        }

        val timetableResource = timetableAsync.await()
        val roomsResource = roomsAsync.await()
        val subjectsResource = subjectsAsync.await()
        val teachersResource = teachersAsync.await()

        val groupClasses = timetableResource.payload?.classes?.filter {  studyLineClass ->
            studyLineClass.groupId == studyGroupId
        }

        val classes = groupClasses?.groupBy { studyLineClass ->
            listOf(
                studyLineClass.id,
                studyLineClass.groupId,
                studyLineClass.day,
                studyLineClass.startHour,
                studyLineClass.endHour,
                studyLineClass.frequencyId,
                studyLineClass.roomId,
                studyLineClass.participantId,
                studyLineClass.participantName,
                studyLineClass.classTypeId,
                studyLineClass.subjectId,
                studyLineClass.teacherId,
            )
        }?.values?.mapNotNull { classes ->
            val studyLineClass = classes.firstOrNull() ?: return@mapNotNull null

            val room = roomsResource.payload?.firstOrNull { room ->
                room.id == studyLineClass.roomId
            } ?: return@mapNotNull null

            val subject = subjectsResource.payload?.firstOrNull { subject ->
                subject.id == studyLineClass.subjectId
            } ?: return@mapNotNull null

            val teacher = teachersResource.payload?.firstOrNull { teacher ->
                teacher.id == studyLineClass.teacherId
            } ?: return@mapNotNull null

            val teacherTitle = TeacherTitle.Companion.getById(teacher.titleId).label
            val participant = classes.joinToString(String.COMMA + String.SPACE) {
                it.participantName
            }

            TimetableClass(
                id = studyLineClass.id,
                day = studyLineClass.day,
                startHour = studyLineClass.startHour,
                endHour = studyLineClass.endHour,
                frequencyId = studyLineClass.frequencyId,
                subject = subject.name,
                classType = ClassType.Companion.getById(studyLineClass.classTypeId),
                participant = participant,
                teacher = "$teacherTitle ${teacher.name}",
                room = room.name
            )
        }

        val studyLine = timetableResource.payload?.studyLine
        val (title, subtitle) = when {
            studyLine != null -> {
                val studyYear =  StudyYear.getById(studyLine.studyYearId)
                studyLine.name to "${studyYear.label} - $studyGroupId"
            }

            else -> return@coroutineScope Resource(null, Status.Error)
        }

        val timetable = Timetable(
            title = title,
            subtitle = subtitle,
            classes = classes ?: return@coroutineScope Resource(null, Status.Error)
        )

        return@coroutineScope Resource(timetable, Status.Success)
    }
}
