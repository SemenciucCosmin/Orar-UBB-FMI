package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class OnboardingFormUiState(
    val studyLevels: ImmutableList<Int> = persistentListOf(),
    val selectedStudyLevel: Int? = null,
    val selectedSemesterId: String? = null,
    val selectedUserTypeId: String? = null,
)

val OnboardingFormUiState.isNextEnabled: Boolean
    get() = listOf(selectedStudyLevel, selectedSemesterId, selectedUserTypeId).all { it != null }
