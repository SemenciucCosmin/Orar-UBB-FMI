package com.ubb.fmi.orar.feature.subjects.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model.SubjectsUiState
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
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
 * @param subjectsDataSource The data source for retrieving subjects.
 * @param timetablePreferences The preferences for the timetable configuration.
 */
class SubjectsViewModel(
    private val subjectsDataSource: SubjectsDataSource,
    private val timetablePreferences: TimetablePreferences,
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
    private val _uiState = MutableStateFlow(SubjectsUiState())
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
        timetablePreferences.getConfiguration().collectLatest { configuration ->
            logger.d(TAG, "getSubjects configuration: $configuration")
            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, errorStatus = ErrorStatus.NOT_FOUND) }
                return@collectLatest
            }

            val resource = subjectsDataSource.getSubjects(
                year = configuration.year,
                semesterId = configuration.semesterId
            )

            logger.d(TAG, "getSubjects resource: $resource")
            _uiState.update {
                it.copy(
                    isLoading = false,
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

    companion object {
        private const val TAG = "SubjectsViewModel"
    }
}
