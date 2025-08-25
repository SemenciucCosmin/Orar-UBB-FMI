package com.ubb.fmi.orar.domain.rooms.usecase

import com.ubb.fmi.orar.data.core.model.ClassType
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.teachers.model.TeacherTitle
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.COMMA
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.timetable.model.Timetable
import com.ubb.fmi.orar.domain.timetable.model.TimetableClass
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlin.String

class GetRoomTimetableUseCase(
    private val roomsDataSource: RoomsDataSource,
    private val subjectsDataSource: SubjectsDataSource,
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences
) {

    suspend operator fun invoke(roomId: String): Resource<Timetable> = coroutineScope {
        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        if (configuration == null) return@coroutineScope Resource(null, Status.Error)

        val timetableAsync = async {
            roomsDataSource.getTimetable(
                year = configuration.year,
                semesterId = configuration.semesterId,
                roomId = roomId
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
        val subjectsResource = subjectsAsync.await()
        val teachersResource = teachersAsync.await()

        val classes = timetableResource.payload?.classes?.groupBy { roomClass ->
            listOf(
                roomClass.id,
                roomClass.day,
                roomClass.startHour,
                roomClass.endHour,
                roomClass.frequencyId,
                roomClass.studyLineId,
                roomClass.participantId,
                roomClass.participantName,
                roomClass.classTypeId,
                roomClass.subjectId,
                roomClass.teacherId,
            )
        }?.values?.mapNotNull { classes ->
            val room = timetableResource.payload.room
            val roomClass = classes.firstOrNull() ?: return@mapNotNull null

            val subject = subjectsResource.payload?.firstOrNull { subject ->
                subject.id == roomClass.subjectId
            } ?: return@mapNotNull null

            val teacher = teachersResource.payload?.firstOrNull { teacher ->
                teacher.id == roomClass.teacherId
            } ?: return@mapNotNull null

            val teacherTitle = TeacherTitle.Companion.getById(teacher.titleId).label
            val participant = classes.joinToString(String.COMMA + String.SPACE) {
                it.participantName
            }

            TimetableClass(
                id = roomClass.id,
                day = roomClass.day,
                startHour = roomClass.startHour,
                endHour = roomClass.endHour,
                frequencyId = roomClass.frequencyId,
                subject = subject.name,
                classType = ClassType.Companion.getById(roomClass.classTypeId),
                participant = participant,
                teacher = "$teacherTitle ${teacher.name}",
                room = room.name
            )
        }

        val room = timetableResource.payload?.room
        val timetable = Timetable(
            subtitle = String.BLANK,
            title = room?.name ?: return@coroutineScope Resource(null, Status.Error),
            classes = classes ?: return@coroutineScope Resource(null, Status.Error)
        )

        return@coroutineScope Resource(timetable, Status.Success)
    }
}
