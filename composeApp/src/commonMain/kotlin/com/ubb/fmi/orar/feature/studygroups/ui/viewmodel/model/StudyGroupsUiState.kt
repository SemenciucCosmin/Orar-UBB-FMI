package com.ubb.fmi.orar.feature.studygroups.ui.viewmodel.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class StudyGroupsUiState(
    val studyGroups: ImmutableList<String> = persistentListOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
