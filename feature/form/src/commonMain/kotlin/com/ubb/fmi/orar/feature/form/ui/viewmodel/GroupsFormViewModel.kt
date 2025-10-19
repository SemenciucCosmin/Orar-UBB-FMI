package com.ubb.fmi.orar.feature.form.ui.viewmodel

import Logger
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.groups.repository.GroupsRepository
import com.ubb.fmi.orar.data.network.model.isEmpty
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.GroupsFromUiState
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.firstOrNull
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for managing the selection of groups in a timetable form.
 * This ViewModel fetches groups based on the provided parameters and allows the user to
 * select a group. It also handles the saving of the selected group configuration
 * and provides a way to retry fetching groups in case of an error.
 */
class GroupsFormViewModel(
    private val groupsRepository: GroupsRepository,
    private val timetablePreferences: TimetablePreferences,
    private val logger: Logger,
) : EventViewModel<GroupsFromUiState.GroupsFromUiEvent>() {

    /**
     * Mutable state flow that holds the UI state for the groups selection.
     * It is initialized with a default state and will be updated as data is fetched.
     */
    private val _uiState = MutableStateFlow(GroupsFromUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()
        .onStart { getGroups() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    /**
     * Initializes the ViewModel and starts fetching groups.
     * This is done in the init block to ensure that groups are fetched as soon as the ViewModel is created.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getGroups() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }

        val configuration = timetablePreferences.getConfiguration().firstOrNull() ?: return@launch
        val studyLevel = configuration.studyLevelId?.let(StudyLevel::getById) ?: return@launch
        val fieldId = configuration.fieldId ?: return@launch
        val studyLineId = fieldId + studyLevel.notation

        groupsRepository.getGroups(studyLineId).collectLatest { resource ->
            logger.d(TAG, "getGroups groups resource: $resource")
            _uiState.update {
                it.copy(
                    isLoading = resource.status.isLoading(),
                    isEmpty = resource.status.isEmpty(),
                    errorStatus = resource.status.toErrorStatus(),
                    groups = resource.payload?.toImmutableList() ?: persistentListOf(),
                    title = resource.payload?.firstOrNull()?.studyLine?.name,
                    studyLevel = studyLevel,
                    selectedGroupId = resource.payload?.firstOrNull { group ->
                        group.id == configuration.groupId
                    }?.id
                )
            }
        }
    }

    /**
     * Selects a group by its ID and updates the UI state.
     * This function is called when the user selects a group from the list.
     * @param groupId: The ID of the group to select.
     */
    fun selectGroup(groupId: String) {
        logger.d(TAG, "selectGroup group: $groupId")
        _uiState.update { it.copy(selectedGroupId = groupId) }
    }

    /**
     * Retries fetching groups in case of an error.
     * This function is called when the user wants to retry fetching groups after an error occurs.
     */
    fun retry() {
        logger.d(TAG, "retry")
        getGroups()
    }

    /**
     * Finishes the group selection process and saves the selected group configuration.
     * This function is called when the user confirms their selection.
     */
    fun finishSelection() {
        logger.d(TAG, "finishSelection")
        viewModelScope.launch {
            _uiState.value.selectedGroupId?.let { groupId ->
                logger.d(TAG, "finishSelection group: $groupId")
                timetablePreferences.setGroupId(groupId)
                registerEvent(GroupsFromUiState.GroupsFromUiEvent.CONFIGURATION_DONE)
            }
        }
    }

    companion object {
        private const val TAG = "GroupsFormViewModel"
    }
}
