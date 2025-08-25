package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.data.core.model.GroupType

data class StudyGroupsFromUiState(
    val studyGroups: List<Group> = emptyList(),
    val selectedStudyGroup: Group? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    data class Group (
        val id: String,
        val type: GroupType
    )
}

val StudyGroupsFromUiState.isNextEnabled: Boolean
    get() = selectedStudyGroup != null