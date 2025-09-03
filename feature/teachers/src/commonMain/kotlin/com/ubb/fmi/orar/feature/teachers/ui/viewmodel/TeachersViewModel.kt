package com.ubb.fmi.orar.feature.teachers.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeachersUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeachersViewModel(
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences,
) : ViewModel() {

    private var job: Job
    private val _uiState = MutableStateFlow(TeachersUiState())
    val uiState = _uiState.asStateFlow()

    init {
        job = getTeachers()
    }

    private fun getTeachers() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, isError = false) }
        timetablePreferences.getConfiguration().collectLatest { configuration ->
            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
                return@collectLatest
            }

            val resource = teachersDataSource.getOwners(
                year = configuration.year,
                semesterId = configuration.semesterId
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = resource.status.isError(),
                    teachers = resource.payload?.toImmutableList() ?: persistentListOf(),
                )
            }
        }
    }

    fun selectTeacherTitleFilter(teacherTitleFilterId: String) {
        _uiState.update {
            it.copy(selectedFilterId = teacherTitleFilterId)
        }
    }

    fun retry() {
        job.cancel()
        job = getTeachers()
    }
}
