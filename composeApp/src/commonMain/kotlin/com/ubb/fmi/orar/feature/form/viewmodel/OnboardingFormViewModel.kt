package com.ubb.fmi.orar.feature.form.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.model.UserType
import com.ubb.fmi.orar.feature.form.viewmodel.model.OnboardingFormUiState
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class OnboardingFormViewModel : EventViewModel<OnboardingFormUiState.OnboardingFormEvent>() {

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
        _uiState.update { it.copy(selectedUserTypeId = userTypeId, selectedDegreeId = null) }
    }

    fun selectDegree(degreeId: String) {
        _uiState.update { it.copy(selectedDegreeId = degreeId) }
    }

    fun finishOnboarding() {
        when (_uiState.value.selectedUserTypeId) {
            UserType.STUDENT.id -> registerEvent(
                event = OnboardingFormUiState.OnboardingFormEvent.ONBOARDING_STUDENT_DONE
            )

            else -> registerEvent(
                event = OnboardingFormUiState.OnboardingFormEvent.ONBOARDING_TEACHER_DONE
            )
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