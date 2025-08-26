package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class StudyGroupsFromUiState(
    val studyGroups: ImmutableList<String> = persistentListOf(),
    val selectedStudyGroupId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)

val StudyGroupsFromUiState.isNextEnabled: Boolean
    get() = selectedStudyGroupId != null