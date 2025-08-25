package com.ubb.fmi.orar.domain.teachers.usecase

import com.ubb.fmi.orar.data.core.model.ClassType
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.teachers.model.TeacherTitle
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.COMMA
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.timetable.model.Timetable
import com.ubb.fmi.orar.domain.timetable.model.TimetableClass
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status
import kotlinx.coroutines.flow.firstOrNull
import kotlin.String

class GetTeacherTimetableUseCase(
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences
) {

    suspend operator fun invoke(teacherId: String): Resource<Timetable> {
        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        if (configuration == null) return Resource(null, Status.Error)

        val timetableResource = teachersDataSource.getTimetable(
            year = configuration.year,
            semesterId = configuration.semesterId,
            teacherId = teacherId
        )

        val classes = timetableResource.payload?.classes?.groupBy { teacherClass ->
            listOf(
                teacherClass.id,
                teacherClass.day,
                teacherClass.startHour,
                teacherClass.endHour,
                teacherClass.frequencyId,
                teacherClass.roomId,
                teacherClass.studyLineId,
                teacherClass.participantId,
                teacherClass.participantName,
                teacherClass.classTypeId,
                teacherClass.subjectId,
                teacherClass.subjectName,
            )
        }?.values?.mapNotNull { classes ->
            val teacher = timetableResource.payload.teacher
            val teacherTitle = TeacherTitle.getById(teacher.titleId)
            val teacherClass = classes.firstOrNull() ?: return@mapNotNull null
            val participant = classes.joinToString(String.COMMA + String.SPACE) {
                it.participantName
            }

            TimetableClass(
                id = teacherClass.id,
                day = teacherClass.day,
                startHour = teacherClass.startHour,
                endHour = teacherClass.endHour,
                frequencyId = teacherClass.frequencyId,
                subject = teacherClass.subjectName,
                classType = ClassType.getById(teacherClass.classTypeId),
                participant = participant,
                teacher = "${teacherTitle.label} ${teacher.name}",
                room = teacherClass.roomId
            )
        }

        val teacher = timetableResource.payload?.teacher
        val title = when {
            teacher != null -> {
                val teacherTitle = TeacherTitle.getById(teacher.titleId)
                "${teacherTitle.label} ${teacher.name}"
            }

            else -> return Resource(null, Status.Error)
        }

        val timetable = Timetable(
            title = title,
            subtitle = String.BLANK,
            classes = classes ?: return Resource(null, Status.Error)
        )

        return Resource(timetable, Status.Success)
    }
}