package com.ubb.fmi.orar.domain.subjects.usecase

import com.ubb.fmi.orar.domain.timetable.model.ClassType
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.teachers.model.TeacherTitle
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.COMMA
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.timetable.model.ClassOwner
import com.ubb.fmi.orar.domain.timetable.model.Day
import com.ubb.fmi.orar.domain.timetable.model.Timetable
import com.ubb.fmi.orar.domain.timetable.model.TimetableClass
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull

class GetSubjectTimetableUseCase(
    private val subjectsDataSource: SubjectsDataSource,
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences
) {

    suspend operator fun invoke(subjectId: String): Resource<Timetable> = coroutineScope {
        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        if (configuration == null) return@coroutineScope Resource(null, Status.Error)

        val timetableAsync = async {
            subjectsDataSource.getTimetable(
                year = configuration.year,
                semesterId = configuration.semesterId,
                subjectId = subjectId
            )
        }

        val teachersAsync = async {
            teachersDataSource.getTeachers(
                year = configuration.year,
                semesterId = configuration.semesterId,
            )
        }

        val timetableResource = timetableAsync.await()
        val teachersResource = teachersAsync.await()

        val classes = timetableResource.payload?.classes?.groupBy { subjectClass ->
            listOf(
                subjectClass.id,
                subjectClass.day,
                subjectClass.startHour,
                subjectClass.endHour,
                subjectClass.frequencyId,
                subjectClass.roomId,
                subjectClass.studyLineId,
                subjectClass.participantId,
                subjectClass.participantName,
                subjectClass.classTypeId,
                subjectClass.teacherId,
            )
        }?.values?.mapNotNull { classes ->
            val subject = timetableResource.payload.subject
            val subjectClass = classes.firstOrNull() ?: return@mapNotNull null
            val teacher = teachersResource.payload?.firstOrNull { teacher ->
                teacher.id == subjectClass.teacherId
            } ?: return@mapNotNull null

            val teacherTitle = TeacherTitle.getById(teacher.titleId).label
            val participant =
                classes.joinToString(String.COMMA + String.SPACE) {
                    it.participantName
                }

            TimetableClass(
                id = subjectClass.id,
                day = subjectClass.day,
                startHour = subjectClass.startHour,
                endHour = subjectClass.endHour,
                frequencyId = subjectClass.frequencyId,
                subject = subject.name,
                classType = ClassType.Companion.getById(subjectClass.classTypeId),
                classOwner = ClassOwner.SUBJECT,
                participant = participant,
                teacher = "$teacherTitle ${teacher.name}",
                room = subjectClass.roomId,
                isVisible = subjectClass.isVisible
            )
        }?.sortedWith(
            compareBy<TimetableClass> { Day.getById(it.day).orderIndex }
                .thenBy { it.startHour }
                .thenBy { it.endHour }
        )

        val subject = timetableResource.payload?.subject
        val timetable = Timetable(
            subtitle = String.BLANK,
            title = subject?.name ?: return@coroutineScope Resource(null, Status.Error),
            classes = classes ?: return@coroutineScope Resource(null, Status.Error),
        )

        return@coroutineScope Resource(timetable, Status.Success)
    }
}