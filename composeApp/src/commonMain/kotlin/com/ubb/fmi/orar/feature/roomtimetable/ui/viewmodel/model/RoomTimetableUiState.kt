package com.ubb.fmi.orar.feature.roomtimetable.ui.viewmodel.model

import com.ubb.fmi.orar.data.core.model.ClassType
import com.ubb.fmi.orar.data.core.model.Frequency
import com.ubb.fmi.orar.data.rooms.model.Room
import com.ubb.fmi.orar.data.rooms.model.RoomClass
import com.ubb.fmi.orar.data.subjects.model.Subject
import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.timetable.ui.model.TimetableItem
import kotlin.collections.component1
import kotlin.collections.component2

data class RoomTimetableUiState(
    private val classes: List<RoomClass> = emptyList(),
    private val teachers: List<Teacher> = emptyList(),
    private val subjects: List<Subject> = emptyList(),
    val room: Room? = null,
    val selectedFrequency: Frequency = Frequency.WEEK_1,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        val RoomTimetableUiState.timetableItems: List<TimetableItem>
            get() {
                return classes.filter { roomClass ->
                    roomClass.frequencyId in listOf(Frequency.BOTH.id, selectedFrequency.id)
                }.groupBy { it.day }.entries.map { (day, classes) ->
                    val divider = TimetableItem.Divider(day)
                    val classItems = classes.mapNotNull { roomClass ->
                        val teacher = teachers.firstOrNull { it.id == roomClass.teacherId }
                        val subject = subjects.firstOrNull { it.id == roomClass.subjectId }

                        TimetableItem.Class(
                            startHour = roomClass.startHour,
                            endHour = roomClass.endHour,
                            subject = subject?.name ?: return@mapNotNull null,
                            classType = ClassType.getById(roomClass.classTypeId),
                            participant = roomClass.participantName,
                            room = room?.name ?: return@mapNotNull null,
                            teacher = teacher?.let { "${it.titleId} ${it.name}" } ?: String.BLANK
                        )
                    }

                    listOf(divider) + classItems
                }.flatten()
            }
    }
}
