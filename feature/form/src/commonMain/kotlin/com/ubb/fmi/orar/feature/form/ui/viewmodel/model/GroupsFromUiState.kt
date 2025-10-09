package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.StudyLevel
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * UiState for group selection screen for timetable configuration
 * @param groups: list of groups for list display
 * @param selectedGroupId: id of group selected by user
 * @param title: screen top bar title
 * @param studyLevel: study level for which [groups] belong to
 * @param isLoading: boolean for loading state
 * @param errorStatus: error state
 */
data class GroupsFromUiState(
    val groups: ImmutableList<Owner.Group> = persistentListOf(),
    val selectedGroupId: String? = null,
    val title: String? = null,
    val studyLevel: StudyLevel? = null,
    val isLoading: Boolean = false,
    val errorStatus: ErrorStatus? = null,
) {
    /**
     * Enum class for one time events on groups form screen
     */
    enum class GroupsFromEvent : Event {
        CONFIGURATION_DONE
    }
}

/**
 * Computed value for specific button on groups form screen
 */
val GroupsFromUiState.isNextEnabled: Boolean
    get() = selectedGroupId != null