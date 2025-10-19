package com.ubb.fmi.orar.feature.groups.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Represents the UI state for the Groups feature in the application.
 * This state holds information about the groups, title, study level,
 * loading status, and error status.
 *
 * @property groups The list of groups available for selection.
 * @property title The title of the groups view.
 * @property studyLevel The study level associated with the groups.
 * @property isLoading Indicates whether the data is currently being loaded.
 * @property errorStatus Indicates whether there was an error loading the data.
 */
data class GroupsUiState(
    val groups: ImmutableList<Owner.Group> = persistentListOf(),
    val title: String? = null,
    val studyLevel: StudyLevel? = null,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val errorStatus: ErrorStatus? = null,
)
