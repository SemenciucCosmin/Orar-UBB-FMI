package com.ubb.fmi.orar.feature.form.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
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

/**
 * ViewModel for managing the onboarding form state in the application.
 * This ViewModel handles the selection of study years, semesters, and user types,
 * and initializes the UI state with the current configuration from preferences.
 *
 * @param timetablePreferences Preferences for managing timetable configurations.
 */
class OnboardingFormViewModel(
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    /**
     * Mutable state flow that holds the UI state for the onboarding form.
     * It is initialized with a default state and will be updated as data is fetched.
     */
    private val _uiState = MutableStateFlow(OnboardingFormUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { loadConfiguration() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    /**
     * Initializes the ViewModel by loading the current configuration from preferences.
     * This method fetches the study years and updates the UI state with the selected
     * study year, semester ID, and user type ID.
     */
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

    /**
     * Selects a study year and updates the UI state accordingly.
     * @param studyYear The year to be selected.
     */
    fun selectStudyYear(studyYear: Int) {
        _uiState.update { it.copy(selectedStudyYear = studyYear) }
    }

    /**
     * Selects a semester by its ID and updates the UI state.
     * @param semesterId The ID of the semester to be selected.
     */
    fun selectSemester(semesterId: String) {
        _uiState.update { it.copy(selectedSemesterId = semesterId) }
    }

    /**
     * Selects a user type by its ID and updates the UI state.
     * @param userTypeId The ID of the user type to be selected.
     */
    fun selectUserType(userTypeId: String) {
        _uiState.update { it.copy(selectedUserTypeId = userTypeId) }
    }

    /**
     * Submits the current configuration to the preferences.
     * This method is called when the user completes the onboarding form.
     */
    @OptIn(ExperimentalTime::class)
    private fun getStudyYears(): List<Int> {
        val currentInstant = Clock.System.now()
        val currentDate = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
        val currentYear = currentDate.year
        val previousYear = currentYear.dec()
        return listOf(previousYear, currentYear)
    }
}