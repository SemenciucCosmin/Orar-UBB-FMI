package com.ubb.fmi.orar.feature.studygroups.ui.viewmodel.model

data class StudyGroupsUiState(
    val studyGroups: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
