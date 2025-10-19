package com.ubb.fmi.orar.feature.settings.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.UiEvent

data class AddPersonalEventUiState(
    val activity: String = String.BLANK,
    val location: String = String.BLANK,
    val caption: String = String.BLANK,
    val details: String = String.BLANK,
    val startHour: Int = DEFAULT_HOUR,
    val startMinute: Int = DEFAULT_MINUTE,
    val endHour: Int = DEFAULT_HOUR,
    val endMinute: Int = DEFAULT_MINUTE,
    val selectedFrequency: Frequency = Frequency.BOTH,
    val selectedDays: List<Day> = emptyList(),
) {
    enum class AddPersonalEventUiEvent : UiEvent {
        SUCCESS
    }

    companion object {
        private const val DEFAULT_HOUR = 12
        private const val DEFAULT_MINUTE = 0
    }
}

val AddPersonalEventUiState.isNextEnabled: Boolean
    get() = activity.isNotBlank() && selectedDays.isNotEmpty()
