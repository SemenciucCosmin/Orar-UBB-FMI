package com.ubb.fmi.orar.feature.form.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.core.model.UserType
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.OnboardingFormUiState
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class OnboardingFormViewModel(
    private val timetablePreferences: TimetablePreferences
) : EventViewModel<OnboardingFormUiState.OnboardingFormEvent>() {

    private val _uiState = MutableStateFlow(OnboardingFormUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { _uiState.update { it.copy(studyYears = getStudyYears()) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    fun selectStudyYear(studyYear: Int) {
        _uiState.update { it.copy(selectedStudyYear = studyYear) }
    }

    fun selectSemester(semesterId: String) {
        _uiState.update { it.copy(selectedSemesterId = semesterId) }
    }

    fun selectUserType(userTypeId: String) {
        _uiState.update { it.copy(selectedUserTypeId = userTypeId) }
    }

    fun finishOnboarding() {
        viewModelScope.launch {
            _uiState.value.selectedStudyYear?.let { timetablePreferences.setYear(it) }
            _uiState.value.selectedSemesterId?.let { timetablePreferences.setSemester(it) }
            _uiState.value.selectedUserTypeId?.let { timetablePreferences.setUserType(it) }

            when (_uiState.value.selectedUserTypeId) {
                UserType.STUDENT.id -> registerEvent(
                    event = OnboardingFormUiState.OnboardingFormEvent.STUDENT_COMPLETED
                )

                else -> registerEvent(
                    event = OnboardingFormUiState.OnboardingFormEvent.TEACHER_COMPLETED
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun getStudyYears(): List<Int> {
        val currentInstant = Clock.System.now()
        val currentDate = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
        val currentYear = currentDate.year
        val previousYear = currentYear.dec()
        return listOf(previousYear, currentYear)
    }
}