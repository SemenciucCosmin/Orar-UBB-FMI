package com.ubb.fmi.orar.feature.form.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.TeachersFormUiState
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeacherTitleFilter
import com.ubb.fmi.orar.ui.catalog.model.UserType
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.domain.timetable.usecase.SetConfigurationUseCase
import com.ubb.fmi.orar.feature.form.ui.model.Semester
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class TeachersFormViewModel(
    private val year: Int,
    private val semesterId: String,
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences,
    private val setConfigurationUseCase: SetConfigurationUseCase,
) : EventViewModel<TeachersFormUiState.TeachersFormEvent>() {

    private val _uiState = MutableStateFlow(TeachersFormUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { getTeachers()}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    fun selectTeacherTitleFilter(teacherTitleFilter: TeacherTitleFilter) {
        _uiState.update {
            it.copy(selectedFilter = teacherTitleFilter)
        }
    }

    private fun getTeachers() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, isError = false) }

        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        val semester = Semester.getById(semesterId)
        val resource = teachersDataSource.getOwners(
            year = year,
            semesterId = semesterId
        )

        val teacher = resource.payload?.firstOrNull{
            it.id == configuration?.teacherId
        }

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = resource.status.isError(),
                teachers = resource.payload?.toImmutableList() ?: persistentListOf(),
                selectedTeacherId = teacher?.id,
                selectedFilter = TeacherTitleFilter.getById(teacher?.titleId),
                title = "$year - ${year.inc()}, ${semester.label}",
            )
        }
    }

    fun selectTeacher(teacherId: String) {
        _uiState.update { it.copy(selectedTeacherId = teacherId) }
    }

    fun retry() {
        getTeachers()
    }

    fun finishSelection() {
        viewModelScope.launch {
            _uiState.value.selectedTeacherId?.let { teacherId ->
                setConfigurationUseCase(
                    year = year,
                    semesterId = semesterId,
                    userTypeId = UserType.TEACHER.id,
                    teacherId = teacherId,
                    fieldId = null,
                    studyLevelId = null,
                    studyLineDegreeId = null,
                    groupId = null
                )

                registerEvent(TeachersFormUiState.TeachersFormEvent.CONFIGURATION_DONE)
            }
        }
    }
}
