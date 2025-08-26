package com.ubb.fmi.orar.feature.teachers.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeacherTitleFilter
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeachersUiState
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

class TeachersViewModel(
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeachersUiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )


    init {
        getTeachers()
    }

    fun selectTeacherTitleFilter(teacherTitleFilter: TeacherTitleFilter) {
        _uiState.update {
            it.copy(selectedFilter = teacherTitleFilter)
        }
    }

    private fun getTeachers() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, isError = false) }

        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        val teachersResource = teachersDataSource.getTeachers(
            year = configuration?.year ?: return@launch,
            semesterId = configuration.semesterId
        )

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = teachersResource.status.isError(),
                teachers = teachersResource.payload?.toImmutableList() ?: persistentListOf()
            )
        }
    }

    fun retry() {
        getTeachers()
    }
}
