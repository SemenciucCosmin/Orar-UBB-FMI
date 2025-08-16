package com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel.model

import com.ubb.fmi.orar.data.core.model.ClassType
import com.ubb.fmi.orar.data.core.model.Frequency
import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.data.teachers.model.TeacherClass
import com.ubb.fmi.orar.data.teachers.model.TeacherTitle
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.COMMA
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.feature.timetable.ui.model.TimetableItem

data class TeacherTimetableUiState(
    private val classes: List<TeacherClass> = emptyList(),
    val teacher: Teacher? = null,
    val selectedFrequency: Frequency = Frequency.WEEK_1,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        val TeacherTimetableUiState.timetableItems: List<TimetableItem>
            get() {
                return classes.filter { teacherClass ->
                    teacherClass.frequencyId in listOf(Frequency.BOTH.id, selectedFrequency.id)
                }.groupBy { it.day }.entries.map { (day, classes) ->
                    val divider = TimetableItem.Divider(day)
                    val classItems = classes.groupBy {
                        TeacherClass(
                            id = String.BLANK,
                            day = it.day,
                            startHour = it.startHour,
                            endHour = it.endHour,
                            frequencyId = it.frequencyId,
                            roomId = it.roomId,
                            studyLineId = String.BLANK,
                            participantId = String.BLANK,
                            participantName = String.BLANK,
                            classTypeId = it.classTypeId,
                            subjectId = it.subjectId,
                            subjectName = it.subjectName
                        )
                    }.mapNotNull { (teacherClassInfo, teacherClasses)->
                        val participant = teacherClasses.joinToString(
                            String.COMMA + String.SPACE
                        ) { it.participantName }

                        val teacherTitle = teacher?.titleId?.let {
                            TeacherTitle.getById(it).label
                        } ?: return@mapNotNull null

                        TimetableItem.Class(
                            startHour = teacherClassInfo.startHour,
                            endHour = teacherClassInfo.endHour,
                            subject = teacherClassInfo.subjectName,
                            classType = ClassType.getById(teacherClassInfo.classTypeId),
                            participant = participant,
                            teacher = "$teacherTitle ${teacher.name}",
                            room = teacherClassInfo.roomId,
                        )
                    }

                    listOf(divider) + classItems
                }.flatten()
            }
    }
}
