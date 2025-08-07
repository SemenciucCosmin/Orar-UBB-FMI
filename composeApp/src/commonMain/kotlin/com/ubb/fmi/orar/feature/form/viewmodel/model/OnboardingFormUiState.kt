package com.ubb.fmi.orar.feature.form.viewmodel.model

import com.ubb.fmi.orar.data.core.model.UserType
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event

data class OnboardingFormUiState(
    val studyYears: List<Int> = emptyList(),
    val selectedStudyYear: Int? = null,
    val selectedSemesterId: String? = null,
    val selectedUserTypeId: String? = null,
    val selectedDegreeId: String? = null,
    val selectedTeacherTitleId: String? = null,
) {
    enum class OnboardingFormEvent : Event {
        FORM_STUDENT_COMPLETED,
        FORM_TEACHER_COMPLETED,
    }
}

val OnboardingFormUiState.isNextEnabled: Boolean
    get() {
        val isStudyYearSelected = selectedStudyYear != null
        val isSemesterSelected = selectedSemesterId != null
        val isDegreeSelected = selectedDegreeId != null
        val isTeacherTitleSelected = selectedTeacherTitleId != null
        val isStudentSelected = selectedUserTypeId == UserType.STUDENT.id
        val isTeacherSelected = selectedUserTypeId == UserType.TEACHER.id

        val isStudentInfoSelected = isStudentSelected && isDegreeSelected
        val isTeacherInfoSelected = isTeacherSelected && isTeacherTitleSelected
        val isUserInfoSelected = isStudentInfoSelected || isTeacherInfoSelected

        return isStudyYearSelected && isSemesterSelected && isUserInfoSelected
    }
