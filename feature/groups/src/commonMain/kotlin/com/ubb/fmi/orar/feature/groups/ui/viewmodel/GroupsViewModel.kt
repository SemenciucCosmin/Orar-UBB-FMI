package com.ubb.fmi.orar.feature.groups.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.groups.repository.GroupsRepository
import com.ubb.fmi.orar.data.network.model.isEmpty
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.feature.groups.ui.viewmodel.model.GroupsUiState
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
 */
class GroupsViewModel(
    private val fieldId: String,
    private val studyLevelId: String,
    private val groupsRepository: GroupsRepository,
    private val analyticsLogger: AnalyticsLogger,
    private val logger: Logger,
) : ViewModel() {

    /**
     * Mutable state flow that holds the UI state for the groups selection.
     * It is initialized with a default state and will be updated as data is fetched.
     */
    private val _uiState = MutableStateFlow(GroupsUiState(isLoading = true))
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
        val studyLineId = fieldId + studyLevel.notation

        groupsRepository.getGroups(studyLineId).collectLatest { resource ->
            logger.d(TAG, "getGroups groupsResource: $resource")
            _uiState.update {
                it.copy(
                    isLoading = resource.status.isLoading(),
                    isEmpty = resource.status.isEmpty(),
                    errorStatus = resource.status.toErrorStatus(),
                    groups = resource.payload?.toImmutableList() ?: persistentListOf(),
                    title = resource.payload?.firstOrNull()?.studyLine?.name,
                    studyLevel = studyLevel
                )
            }
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

    /**
     * Registers analytics event for the item click action
     */
    fun handleClickAction() {
        analyticsLogger.logEvent(AnalyticsEvent.VIEW_TIMETABLE_GROUP)
    }

    companion object {
        private const val TAG = "GroupsViewModel"
    }
}
