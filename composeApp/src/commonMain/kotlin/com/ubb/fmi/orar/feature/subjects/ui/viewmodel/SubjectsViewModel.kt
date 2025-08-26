package com.ubb.fmi.orar.feature.subjects.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model.SubjectsUiState
import com.ubb.fmi.orar.network.model.isError
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SubjectsViewModel(
    private val subjectsDataSource: SubjectsDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubjectsUiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )


    init {
        getSubjects()
    }

    fun setSearchQuery(searchQuery: String) {
        _uiState.update {
            it.copy(searchQuery = searchQuery)
        }
    }

    private fun getSubjects() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                isLoading = true,
                isError = false
            )
        }
       
        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        val subjectResource = subjectsDataSource.getSubjects(
            year = configuration?.year ?: return@launch,
            semesterId = configuration.semesterId
        )

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = subjectResource.status.isError(),
                subjects = subjectResource.payload?.toImmutableList() ?: persistentListOf()
            )
        }
    }

    fun retry() {
        getSubjects()
    }
}
