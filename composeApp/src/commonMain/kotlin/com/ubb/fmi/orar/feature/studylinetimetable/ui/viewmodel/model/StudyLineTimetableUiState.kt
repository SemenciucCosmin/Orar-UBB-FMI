package com.ubb.fmi.orar.feature.studylinetimetable.ui.viewmodel.model

import com.ubb.fmi.orar.data.core.model.ClassType
import com.ubb.fmi.orar.data.core.model.Frequency
import com.ubb.fmi.orar.data.rooms.model.Room
import com.ubb.fmi.orar.data.studyline.model.StudyLine
import com.ubb.fmi.orar.data.studyline.model.StudyLineClass
import com.ubb.fmi.orar.data.subjects.model.Subject
import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.timetable.ui.model.TimetableItem
import kotlin.collections.component1
import kotlin.collections.component2

data class StudyLineTimetableUiState(
    private val classes: List<StudyLineClass> = emptyList(),
    private val teachers: List<Teacher> = emptyList(),
    private val subjects: List<Subject> = emptyList(),
    private val rooms: List<Room> = emptyList(),
    val studyLine: StudyLine? = null,
    val selectedFrequency: Frequency = Frequency.WEEK_1,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        val StudyLineTimetableUiState.timetableItems: List<TimetableItem>
            get() {
                return classes.filter { studyLineClass ->
                    studyLineClass.frequencyId in listOf(Frequency.BOTH.id, selectedFrequency.id)
                }.groupBy { it.day }.entries.map { (day, classes) ->
                    val divider = TimetableItem.Divider(day)
                    val classItems = classes.mapNotNull { studyLineClass ->
                        val teacher = teachers.firstOrNull { it.id == studyLineClass.teacherId }
                        val subject = subjects.firstOrNull { it.id == studyLineClass.subjectId }
                        val room = rooms.firstOrNull { it.id == studyLineClass.roomId }

                        TimetableItem.Class(
                            startHour = studyLineClass.startHour,
                            endHour = studyLineClass.endHour,
                            subject = subject?.name ?: return@mapNotNull null,
                            classType = ClassType.getById(studyLineClass.classTypeId),
                            participant = studyLineClass.participantName,
                            room = room?.name ?: return@mapNotNull null,
                            teacher = teacher?.let { "${it.titleId} ${it.name}" } ?: String.BLANK
                        )
                    }

                    listOf(divider) + classItems
                }.flatten()
            }
    }
}
