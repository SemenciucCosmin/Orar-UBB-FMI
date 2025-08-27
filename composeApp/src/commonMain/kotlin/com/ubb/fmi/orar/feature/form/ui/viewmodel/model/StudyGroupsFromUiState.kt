package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class StudyGroupsFromUiState(
    val studyGroups: ImmutableList<String> = persistentListOf(),
    val selectedStudyGroupId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    enum class StudyGroupsFromEvent : Event {
        CONFIGURATION_DONE
    }
}

val StudyGroupsFromUiState.isNextEnabled: Boolean
    get() = selectedStudyGroupId != null