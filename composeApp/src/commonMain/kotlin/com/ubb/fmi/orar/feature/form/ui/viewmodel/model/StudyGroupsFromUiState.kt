package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

data class StudyGroupsFromUiState(
    val studyGroups: List<String> = emptyList(),
    val selectedStudyGroupId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)

val StudyGroupsFromUiState.isNextEnabled: Boolean
    get() = selectedStudyGroupId != null