package com.ubb.fmi.orar.feature.form.viewmodel.model

import com.ubb.fmi.orar.data.model.UserType
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event

data class OnboardingFormUiState(
    val studyYears: List<Int> = emptyList(),
    val selectedStudyYear: Int? = null,
    val selectedSemesterId: String? = null,
    val selectedUserTypeId: String? = null,
    val selectedDegreeId: String? = null,
){
    enum class OnboardingFormEvent: Event {
        ONBOARDING_STUDENT_DONE,
        ONBOARDING_TEACHER_DONE,
    }
}

val OnboardingFormUiState.isNextEnabled: Boolean
    get() {
        val isStudyYearSelected = selectedStudyYear != null
        val isSemesterSelected = selectedSemesterId != null
        val isDegreeSelected = selectedDegreeId != null
        val isStudentSelected = selectedUserTypeId == UserType.STUDENT.id
        val isTeacherSelected = selectedUserTypeId == UserType.TEACHER.id
        val isUserInfoSelected = isTeacherSelected || (isStudentSelected && isDegreeSelected)
        return isStudyYearSelected && isSemesterSelected && isUserInfoSelected
    }
