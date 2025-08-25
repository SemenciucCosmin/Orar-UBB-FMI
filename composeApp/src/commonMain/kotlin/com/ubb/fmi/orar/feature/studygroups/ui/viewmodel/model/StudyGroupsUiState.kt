package com.ubb.fmi.orar.feature.studygroups.ui.viewmodel.model

import com.ubb.fmi.orar.data.core.model.GroupType

data class StudyGroupsUiState(
    val studyGroups: List<Group> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    data class Group (
        val id: String,
        val type: GroupType
    )
}
