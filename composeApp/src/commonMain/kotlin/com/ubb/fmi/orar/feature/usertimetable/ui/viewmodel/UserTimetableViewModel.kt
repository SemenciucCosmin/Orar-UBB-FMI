package com.ubb.fmi.orar.feature.usertimetable.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.domain.timetable.usecase.ChangeTimetableClassVisibilityUseCase
import com.ubb.fmi.orar.ui.catalog.model.Frequency
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetUserTimetableUseCase
import com.ubb.fmi.orar.ui.catalog.model.TimetableListItem
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
import com.ubb.fmi.orar.data.network.model.isError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class UserTimetableViewModel(
    private val getUserTimetableUseCase: GetUserTimetableUseCase,
    private val changeTimetableClassVisibilityUseCase: ChangeTimetableClassVisibilityUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimetableUiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    init {
        loadTimetable()
    }

    private fun loadTimetable() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }
            val timetableResource = getUserTimetableUseCase()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = timetableResource.status.isError(),
                    timetable = timetableResource.payload,
                )
            }
        }
    }

    fun selectFrequency(frequency: Frequency) {
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    fun changeEditMode() {
        _uiState.update { it.copy(isEditModeOn = !it.isEditModeOn) }
    }

    fun changeTimetableClassVisibility(timetableClass: TimetableListItem.Class) {
        viewModelScope.launch {
            changeTimetableClassVisibilityUseCase(
                timetableClassId = timetableClass.id,
                timetableClassOwner = timetableClass.classOwner,
            )
        }

        _uiState.update { state ->
            val newClasses = state.timetable?.classes?.map {
                when {
                    it.id != timetableClass.id -> it
                    else -> it.copy(isVisible = !it.isVisible)
                }
            } ?: emptyList()

            state.copy(timetable = state.timetable?.copy(classes = newClasses))
        }
    }

    fun retry() {
        loadTimetable()
    }
}