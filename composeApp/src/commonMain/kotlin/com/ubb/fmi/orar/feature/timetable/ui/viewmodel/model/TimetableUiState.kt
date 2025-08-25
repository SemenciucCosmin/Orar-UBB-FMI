package com.ubb.fmi.orar.feature.timetable.ui.viewmodel.model

import com.ubb.fmi.orar.data.core.model.Frequency
import com.ubb.fmi.orar.domain.timetable.model.Timetable
import com.ubb.fmi.orar.feature.timetable.ui.model.TimetableListItem

data class TimetableUiState(
    val timetable: Timetable? = null,
    val selectedFrequency: Frequency = Frequency.WEEK_1,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        val TimetableUiState.timetableListItems: List<TimetableListItem>
            get() {
                val filteredClasses = timetable?.classes?.filter { timetableClass ->
                    timetableClass.frequencyId in listOf(Frequency.BOTH.id, selectedFrequency.id)
                }

                return filteredClasses?.groupBy { it.day }?.map { (day, classes) ->
                    val divider = TimetableListItem.Divider(day)
                    val classItems = classes.map { timetableClass ->
                        TimetableListItem.Class(
                            startHour = timetableClass.startHour,
                            endHour = timetableClass.endHour,
                            subject = timetableClass.subject,
                            classType = timetableClass.classType,
                            participant = timetableClass.participant,
                            teacher = timetableClass.teacher,
                            room = timetableClass.room,
                        )
                    }

                    listOf(divider) + classItems
                }?.flatten() ?: emptyList()
            }
    }
}
