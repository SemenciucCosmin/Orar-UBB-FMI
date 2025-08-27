package com.ubb.fmi.orar.feature.form.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.OnboardingFormUiState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingFormUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { loadConfiguration() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    private fun loadConfiguration() {
        viewModelScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            _uiState.update {
                it.copy(
                    studyYears = getStudyYears().toImmutableList(),
                    selectedStudyYear = configuration?.year,
                    selectedSemesterId = configuration?.semesterId,
                    selectedUserTypeId = configuration?.userTypeId
                )
            }
        }
    }

    fun selectStudyYear(studyYear: Int) {
        _uiState.update { it.copy(selectedStudyYear = studyYear) }
    }

    fun selectSemester(semesterId: String) {
        _uiState.update { it.copy(selectedSemesterId = semesterId) }
    }

    fun selectUserType(userTypeId: String) {
        _uiState.update { it.copy(selectedUserTypeId = userTypeId) }
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