package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.ui.catalog.model.StudyLevel
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class GroupsFromUiState(
    val groups: ImmutableList<String> = persistentListOf(),
    val selectedGroupId: String? = null,
    val title: String? = null,
    val studyLevel: StudyLevel? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    enum class GroupsFromEvent : Event {
        CONFIGURATION_DONE
    }
}

val GroupsFromUiState.isNextEnabled: Boolean
    get() = selectedGroupId != null