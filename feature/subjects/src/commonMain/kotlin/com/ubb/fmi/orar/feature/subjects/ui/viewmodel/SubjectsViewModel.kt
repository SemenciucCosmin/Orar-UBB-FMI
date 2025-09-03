package com.ubb.fmi.orar.feature.subjects.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model.SubjectsUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubjectsViewModel(
    private val subjectsDataSource: SubjectsDataSource,
    private val timetablePreferences: TimetablePreferences,
) : ViewModel() {

    private var job: Job
    private val _uiState = MutableStateFlow(SubjectsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        job = getSubjects()
    }

    fun setSearchQuery(searchQuery: String) {
        _uiState.update {
            it.copy(searchQuery = searchQuery)
        }
    }

    private fun getSubjects() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, isError = false) }
        timetablePreferences.getConfiguration().collectLatest { configuration ->
            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
                return@collectLatest
            }

            val resource = subjectsDataSource.getOwners(
                year = configuration.year,
                semesterId = configuration.semesterId
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = resource.status.isError(),
                    subjects = resource.payload?.toImmutableList() ?: persistentListOf()
                )
            }
        }
    }

    fun retry() {
        job.cancel()
        job = getSubjects()
    }
}
