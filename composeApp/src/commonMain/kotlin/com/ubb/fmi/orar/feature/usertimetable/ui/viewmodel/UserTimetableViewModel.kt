package com.ubb.fmi.orar.feature.usertimetable.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.domain.timetable.usecase.ChangeTimetableClassVisibilityUseCase
import com.ubb.fmi.orar.ui.catalog.model.Frequency
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetUserTimetableUseCase
import com.ubb.fmi.orar.ui.catalog.model.TimetableListItem
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
import com.ubb.fmi.orar.data.network.model.isError
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserTimetableViewModel(
    private val getUserTimetableUseCase: GetUserTimetableUseCase,
    private val changeTimetableClassVisibilityUseCase: ChangeTimetableClassVisibilityUseCase,
) : ViewModel() {

    private var job: Job
    private val _uiState = MutableStateFlow(TimetableUiState())
    val uiState = _uiState.asStateFlow()

    init {
        job = loadTimetable()
    }

    private fun loadTimetable() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, isError = false) }
        getUserTimetableUseCase().collectLatest { resource ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = resource.status.isError(),
                    classes = resource.payload?.classes?.toImmutableList() ?: persistentListOf()
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
                timetableOwnerType = timetableClass.timetableOwnerType,
            )
        }

        _uiState.update { state ->
            val newClasses = state.classes.map {
                when {
                    it.id != timetableClass.id -> it
                    else -> it.copy(isVisible = !it.isVisible)
                }
            }.toImmutableList()

            state.copy(classes = newClasses)
        }
    }

    fun retry() {
        job.cancel()
        job = loadTimetable()
    }
}