package com.ubb.fmi.orar.feature.form.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.feature.form.viewmodel.model.TeachersFormUiState
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.isError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class TeachersFormViewModel(
    private val teacherTitleId: String,
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private var job: Job
    private val _uiState = MutableStateFlow(TeachersFormUiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    init {
        job = getTeachers()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getTeachers() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        timetablePreferences.getConfiguration().filterNotNull().transformLatest { configuration ->
            val teachersResource = teachersDataSource.getTeachers(
                year = configuration.year,
                semesterId = configuration.semesterId
            )
            val filteredTeachers = teachersResource.payload?.filter { teacher ->
                teacher.titleId == teacherTitleId
            }

            emit(Resource(filteredTeachers, teachersResource.status))
        }.collectLatest { teachersResource ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = teachersResource.status.isError(),
                    teachers = teachersResource.payload ?: emptyList()
                )
            }
        }
    }

    fun selectTeacher(teacherId: String) {
        _uiState.update { it.copy(selectedTeacherId = teacherId) }
    }

    fun retry() {
        job.cancel()
        job = getTeachers()
    }
}
