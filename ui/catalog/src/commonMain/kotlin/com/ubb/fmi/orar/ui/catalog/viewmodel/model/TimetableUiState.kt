package com.ubb.fmi.orar.ui.catalog.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.TimetableClass
import com.ubb.fmi.orar.data.timetable.model.TimetableOwnerType
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.COMMA
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.ui.catalog.model.StudyLevel
import com.ubb.fmi.orar.ui.catalog.model.ClassType
import com.ubb.fmi.orar.ui.catalog.model.Day
import com.ubb.fmi.orar.ui.catalog.model.Frequency
import com.ubb.fmi.orar.ui.catalog.model.TimetableListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.String

/**
 * Represents the UI state of the timetable, including the list of classes, title, study level,
 * group, selected frequency, edit mode status, loading status, and error status.
 * This state is used to manage the display and interaction of the timetable in the UI.
 * @property classes The list of classes in the timetable.
 * @property title The title of the timetable, typically representing the academic program or semester.
 * @property studyLevel The study level associated with the timetable, such as first year, second year, etc.
 * @property group The group identifier for the classes in the timetable.
 * @property selectedFrequency The frequency of classes to be displayed, such as weekly or bi-weekly.
 * @property isEditModeOn Indicates whether the timetable is in edit mode, allowing modifications
 * @property isLoading Indicates whether the timetable data is currently being loaded.
 * @property isError Indicates whether there was an error loading the timetable data.
 */
data class TimetableUiState(
    val classes: ImmutableList<TimetableClass> = persistentListOf(),
    val title: String = String.BLANK,
    val studyLevel: StudyLevel? = null,
    val group: String? = null,
    val selectedFrequency: Frequency = Frequency.WEEK_1,
    val isEditModeOn: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        /**
         * Creates an initial state for the timetable UI.
         * This state is used when the timetable is first loaded or reset.
         */
        val TimetableUiState.timetableListItems: ImmutableList<TimetableListItem>
            get() {
                val filteredClasses = classes.filter { timetableClass ->
                    timetableClass.frequencyId in listOf(Frequency.BOTH.id, selectedFrequency.id)
                }

                val classes = when {
                    isEditModeOn -> filteredClasses
                    else -> {
                        val visibleClasses = filteredClasses.filter { it.isVisible }
                        val groupedClasses = visibleClasses.groupBy { timetableClass ->
                            listOf(
                                timetableClass.day,
                                timetableClass.startHour,
                                timetableClass.endHour,
                                timetableClass.room,
                                timetableClass.classType,
                                timetableClass.ownerId,
                                timetableClass.groupId,
                                timetableClass.ownerTypeId,
                                timetableClass.subject,
                                timetableClass.teacher,
                            )
                        }

                        groupedClasses.values.mapNotNull { classes ->
                            val joinedParticipant = classes.joinToString(
                                String.COMMA + String.SPACE
                            ) { it.participant }

                            classes.firstOrNull()?.copy(participant = joinedParticipant)
                        }
                    }
                }

                return classes.groupBy { it.day }.map { (day, classes) ->
                    val divider = TimetableListItem.Divider(Day.getById(day))
                    val classItems = classes.map { timetableClass ->

                        TimetableListItem.Class(
                            id = timetableClass.id,
                            startHour = timetableClass.startHour,
                            endHour = timetableClass.endHour,
                            subject = timetableClass.subject,
                            classType = ClassType.getById(timetableClass.classType),
                            timetableOwnerType = TimetableOwnerType.getById(timetableClass.ownerTypeId),
                            participant = timetableClass.participant,
                            teacher = timetableClass.teacher,
                            room = timetableClass.room,
                            isVisible = timetableClass.isVisible,
                        )
                    }

                    listOf(divider) + classItems
                }.flatten().toImmutableList()
            }
    }
}