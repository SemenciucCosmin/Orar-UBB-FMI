package com.ubb.fmi.orar.feature.timetable.ui.viewmodel.model

import com.ubb.fmi.orar.feature.timetable.ui.model.Frequency
import com.ubb.fmi.orar.domain.timetable.model.Timetable
import com.ubb.fmi.orar.feature.timetable.ui.model.TimetableListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class TimetableUiState(
    val timetable: Timetable? = null,
    val selectedFrequency: Frequency = Frequency.WEEK_1,
    val isEditModeOn: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        val TimetableUiState.timetableListItems: ImmutableList<TimetableListItem>
            get() {
                val filteredClasses = timetable?.classes?.filter { timetableClass ->
                    timetableClass.frequencyId in listOf(Frequency.BOTH.id, selectedFrequency.id)
                }?.filter { isEditModeOn || it.isVisible }

                return filteredClasses?.groupBy { it.day }?.map { (day, classes) ->
                    val divider = TimetableListItem.Divider(day)
                    val classItems = classes.map { timetableClass ->
                        TimetableListItem.Class(
                            id = timetableClass.id,
                            startHour = timetableClass.startHour,
                            endHour = timetableClass.endHour,
                            subject = timetableClass.subject,
                            classType = timetableClass.classType,
                            classOwner = timetableClass.classOwner,
                            participant = timetableClass.participant,
                            teacher = timetableClass.teacher,
                            room = timetableClass.room,
                            isVisible = timetableClass.isVisible,
                        )
                    }

                    listOf(divider) + classItems
                }?.flatten()?.toImmutableList() ?: persistentListOf()
            }
    }
}
