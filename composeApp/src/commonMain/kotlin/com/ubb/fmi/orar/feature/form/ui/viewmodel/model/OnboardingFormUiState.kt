package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class OnboardingFormUiState(
    val studyYears: ImmutableList<Int> = persistentListOf(),
    val selectedStudyYear: Int? = null,
    val selectedSemesterId: String? = null,
    val selectedUserTypeId: String? = null,
) {
    enum class OnboardingFormEvent : Event {
        STUDENT_COMPLETED,
        TEACHER_COMPLETED
    }
}

val OnboardingFormUiState.isNextEnabled: Boolean
    get() = listOfNotNull(selectedStudyYear, selectedSemesterId, selectedUserTypeId).isNotEmpty()
