package com.ubb.fmi.orar.feature.groups.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.students.datasource.GroupsDataSource
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.groups.ui.viewmodel.model.GroupsUiState
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.StudyLevel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for managing the groups selection in a timetable.
 * This ViewModel fetches groups based on the provided field ID and study level ID,
 * and allows the user to select a group. It also handles loading state and error state.
 *
 * @param fieldId The ID of the field of study.
 * @param studyLevelId The ID of the study level (e.g., Bachelor, Master).
 * @param groupsDataSource The data source for fetching groups.
 * @param timetablePreferences Preferences for managing timetable configurations.
 */
class GroupsViewModel(
    private val fieldId: String,
    private val studyLevelId: String,
    private val groupsDataSource: GroupsDataSource,
    private val timetablePreferences: TimetablePreferences,
    private val logger: Logger,
) : ViewModel() {

    /**
     * Mutable state flow that holds the UI state for the groups selection.
     * It is initialized with a default state and will be updated as data is fetched.
     */
    private val _uiState = MutableStateFlow(GroupsUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { getGroups() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    /**
     * Initializes the ViewModel by fetching the groups based on the provided field ID and study level ID.
     * This method updates the UI state with the fetched groups, title, and study level.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getGroups() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }

        val studyLevel = StudyLevel.getById(studyLevelId)
        val lineId = fieldId + studyLevel.notation

        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        logger.d(TAG, "getGroups configuration: $configuration")

        val groupsResource = groupsDataSource.getGroups(
            year = configuration?.year ?: return@launch,
            semesterId = configuration.semesterId,
            studyLineId = lineId
        )

        logger.d(TAG, "getGroups groupsResource: $groupsResource")
        _uiState.update {
            it.copy(
                isLoading = false,
                errorStatus = groupsResource.status.toErrorStatus(),
                groups = groupsResource.payload?.toImmutableList() ?: persistentListOf(),
                title = groupsResource.payload?.firstOrNull()?.studyLine?.name,
                studyLevel = studyLevel
            )
        }
    }

    /**
     * Selects a group and updates the UI state accordingly.
     * This method also saves the selected group in the timetable preferences.
     */
    fun retry() {
        logger.d(TAG, "retry")
        getGroups()
    }

    companion object {
        private const val TAG = "GroupsViewModel"
    }
}
