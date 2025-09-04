package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

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
)

/**
 * Computed value for specific button on onboarding form screen
 */
val OnboardingFormUiState.isNextEnabled: Boolean
    get() = listOf(selectedStudyYear, selectedSemesterId, selectedUserTypeId).all { it != null }
