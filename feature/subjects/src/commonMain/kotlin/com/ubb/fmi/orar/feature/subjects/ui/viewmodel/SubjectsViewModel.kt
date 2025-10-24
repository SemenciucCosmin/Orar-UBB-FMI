package com.ubb.fmi.orar.feature.subjects.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.subjects.repository.SubjectsRepository
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model.SubjectsUiState
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the state of the Subjects screen.
 * It retrieves subjects from the data source and provides functionality to filter them based
 * on a search query. It also handles loading and error states.
 */
class SubjectsViewModel(
    private val subjectsRepository: SubjectsRepository,
    private val analyticsLogger: AnalyticsLogger,
    private val logger: Logger,
) : ViewModel() {

    /**
     * Job to manage the coroutine for fetching subjects.
     * This allows for cancellation and restarting of the data fetching process.
     */
    private var job: Job

    /**
     * Mutable state flow that holds the current UI state of the Subjects screen.
     * It is updated with the latest subjects, search query, and loading/error states.
     */
    private val _uiState = MutableStateFlow(SubjectsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    /**
     * Initializes the ViewModel by starting the coroutine to fetch subjects.
     * This is done in the init block to ensure it runs when the ViewModel is created.
     */
    init {
        job = getSubjects()
    }

    /**
     * Sets the search query for filtering subjects.
     * This updates the UI state with the new search query, which will be used to
     * filter the subjects list.
     * @param searchQuery The new search query to set.
     */
    fun setSearchQuery(searchQuery: String) {
        logger.d(TAG, "setSearchQuery: $searchQuery")
        _uiState.update {
            it.copy(searchQuery = searchQuery)
        }
    }

    /**
     * Fetches the subjects from the data source and updates the UI state.
     * It collects the timetable configuration and retrieves subjects based on the year and semester ID.
     * The UI state is updated with the loading status, error status, and the list of subjects.
     */
    private fun getSubjects() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }
        subjectsRepository.getSubjects().collectLatest { resource ->
            logger.d(TAG, "getSubjects resource: $resource")
            _uiState.update {
                it.copy(
                    isLoading = resource.status.isLoading(),
                    errorStatus = resource.status.toErrorStatus(),
                    subjects = resource.payload?.toImmutableList() ?: persistentListOf()
                )
            }
        }
    }

    /**
     * Retries fetching the subjects by canceling the current job and starting a new one.
     * This is useful in case of an error or when the user wants to refresh the subjects list.
     */
    fun retry() {
        logger.d(TAG, "retry")
        job.cancel()
        job = getSubjects()
    }

    /**
     * Registers analytics event for the item click action
     */
    fun handleClickAction() {
        analyticsLogger.logEvent(AnalyticsEvent.VIEW_TIMETABLE_SUBJECT)
    }


    companion object {
        private const val TAG = "SubjectsViewModel"
    }
}
