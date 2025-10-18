package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.ui.catalog.viewmodel.model.UiEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * UiState for onboarding form screen for timetable configuration
 * @param studyYears: list of years
 * @param selectedStudyYear: selected year
 * @param selectedSemesterId: selected semester
 * @param selectedUserTypeId: selected super type
 */
data class OnboardingFormUiState(
    val studyYears: ImmutableList<Int> = persistentListOf(),
    val selectedStudyYear: Int? = null,
    val selectedSemesterId: String? = null,
    val selectedUserTypeId: String? = null,
) {
    enum class OnboardingFormUiEvent: UiEvent {
        STUDENT_FINISH,
        TEACHER_FINISH
    }
}

/**
 * Computed value for specific button on onboarding form screen
 */
val OnboardingFormUiState.isNextEnabled: Boolean
    get() = listOf(selectedStudyYear, selectedSemesterId, selectedUserTypeId).all { it != null }
