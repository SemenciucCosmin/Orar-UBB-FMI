package com.ubb.fmi.orar.feature.subjecttimetable.ui.viewmodel.model

import com.ubb.fmi.orar.data.core.model.ClassType
import com.ubb.fmi.orar.data.core.model.Frequency
import com.ubb.fmi.orar.data.subjects.model.Subject
import com.ubb.fmi.orar.data.subjects.model.SubjectClass
import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.timetable.ui.model.TimetableItem

data class SubjectTimetableUiState(
    private val classes: List<SubjectClass> = emptyList(),
    private val teachers: List<Teacher> = emptyList(),
    val subject: Subject? = null,
    val selectedFrequency: Frequency = Frequency.WEEK_1,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        val SubjectTimetableUiState.timetableItems: List<TimetableItem>
            get() {
                return classes.filter { subjectClass ->
                    subjectClass.frequencyId in listOf(Frequency.BOTH.id, selectedFrequency.id)
                }.groupBy { it.day }.entries.map { (day, classes) ->
                    val divider = TimetableItem.Divider(day)
                    val classItems = classes.mapNotNull { subjectClass ->
                        val teacher = teachers.firstOrNull{ it.id == subjectClass.teacherId}

                        TimetableItem.Class(
                            startHour = subjectClass.startHour,
                            endHour = subjectClass.endHour,
                            subject = subject?.name ?: return@mapNotNull null,
                            classType = ClassType.getById(subjectClass.classTypeId),
                            participant = subjectClass.participantName,
                            room = subjectClass.roomId,
                            teacher = teacher?.let { "${it.titleId} ${it.name}" } ?: String.BLANK
                        )
                    }

                    listOf(divider) + classItems
                }.flatten()
            }
    }
}
