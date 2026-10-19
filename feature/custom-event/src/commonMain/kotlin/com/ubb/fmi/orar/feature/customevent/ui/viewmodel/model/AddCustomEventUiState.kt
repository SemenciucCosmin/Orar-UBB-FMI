package com.ubb.fmi.orar.feature.customevent.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.UiEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AddCustomEventUiState(
    val activity: String = String.BLANK,
    val location: String = String.BLANK,
    val caption: String = String.BLANK,
    val details: String = String.BLANK,
    val startHour: Int = DEFAULT_HOUR,
    val startMinute: Int = DEFAULT_MINUTE,
    val endHour: Int = DEFAULT_END_HOUR,
    val endMinute: Int = DEFAULT_MINUTE,
    val selectedFrequency: Frequency = Frequency.BOTH,
    val selectedDays: ImmutableList<Day> = persistentListOf(Day.MONDAY),
) {
    enum class AddCustomEventUiEvent : UiEvent {
        SUCCESS
    }

    companion object {
        private const val DEFAULT_HOUR = 12
        private const val DEFAULT_END_HOUR = 14
        private const val DEFAULT_MINUTE = 0
    }
}

val AddCustomEventUiState.isTimeIntervalValid: Boolean
    get() = endHour * MINUTES_IN_HOUR + endMinute > startHour * MINUTES_IN_HOUR + startMinute

val AddCustomEventUiState.isNextEnabled: Boolean
    get() = activity.isNotBlank() && selectedDays.isNotEmpty() && isTimeIntervalValid

private const val MINUTES_IN_HOUR = 60
