package com.ubb.fmi.orar.feature.personalevent.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.domain.usertimetable.usecase.AddPersonalEventsUseCase
import com.ubb.fmi.orar.feature.personalevent.ui.viewmodel.model.AddPersonalEventUiState
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for theme settings selection
 * Fetches selected theme option from preferences
 * Saves a new theme option to preferences
 */
@Suppress("TooManyFunctions")
class AddPersonalEventViewModel(
    private val addPersonalEventsUseCase: AddPersonalEventsUseCase
) : EventViewModel<AddPersonalEventUiState.AddPersonalEventUiEvent>() {

    private val _uiState = MutableStateFlow(AddPersonalEventUiState())
    val uiState = _uiState.asStateFlow()

    fun changeActivity(activity: String) {
        _uiState.update { it.copy(activity = activity) }
    }

    fun changeCaption(caption: String) {
        _uiState.update { it.copy(caption = caption) }
    }

    fun changeLocation(location: String) {
        _uiState.update { it.copy(location = location) }
    }

    fun changeDetails(details: String) {
        _uiState.update { it.copy(details = details) }
    }

    fun changeStartHour(startHour: Int) {
        _uiState.update { it.copy(startHour = startHour) }
    }

    fun changeStartMinute(startMinute: Int) {
        _uiState.update { it.copy(startMinute = startMinute) }
    }

    fun changeEndHour(endHour: Int) {
        _uiState.update { it.copy(endHour = endHour) }
    }

    fun changeEndMinute(endMinute: Int) {
        _uiState.update { it.copy(endMinute = endMinute) }
    }

    fun selectFrequency(frequency: Frequency) {
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    fun selectDays(days: List<Day>) {
        _uiState.update { it.copy(selectedDays = days) }
    }

    fun finish() {
        viewModelScope.launch {
            addPersonalEventsUseCase(
                activity = uiState.value.activity,
                location = uiState.value.location,
                caption = uiState.value.caption,
                details = uiState.value.details,
                startHour = uiState.value.startHour,
                startMinute = uiState.value.startMinute,
                endHour = uiState.value.endHour,
                endMinute = uiState.value.endMinute,
                frequency = uiState.value.selectedFrequency,
                days = uiState.value.selectedDays,
            )

            registerEvent(AddPersonalEventUiState.AddPersonalEventUiEvent.SUCCESS)
        }
    }
}