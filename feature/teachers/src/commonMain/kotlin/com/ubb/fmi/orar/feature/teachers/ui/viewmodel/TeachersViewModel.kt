package com.ubb.fmi.orar.feature.teachers.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeachersUiState
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
 * ViewModel for managing the state of the teachers screen.
 * It interacts with the TeachersDataSource to fetch teacher data and updates the UI state accordingly.
 * It also handles user interactions such as selecting a teacher title filter and retrying data fetches.
 * @property teachersDataSource The data source for fetching teacher information.
 * @property timetablePreferences The preferences for the timetable configuration.
 */
class TeachersViewModel(
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences,
    private val logger: Logger,
) : ViewModel() {

    /**
     * A Job that represents the ongoing data fetch operation for teachers.
     * It can be cancelled to stop the operation if needed.
     */
    private var job: Job

    /**
     * A MutableStateFlow that holds the current UI state of the teachers screen.
     * It is updated with loading, error, and teacher data as it becomes available.
     */
    private val _uiState = MutableStateFlow(TeachersUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Initializes the ViewModel by starting the data fetch operation for teachers.
     * It sets the initial UI state to loading and updates it with the fetched data.
     */
    init {
        job = getTeachers()
    }

    /**
     * Fetches the list of teachers from the data source and updates the UI state.
     * It collects the timetable configuration and uses it to retrieve the teachers.
     * If the configuration is null or an error occurs, it updates the UI state accordingly.
     */
    private fun getTeachers() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }
        timetablePreferences.getConfiguration().collectLatest { configuration ->
            logger.d(TAG, "getTeachers configuration: $configuration")
            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, errorStatus = ErrorStatus.NOT_FOUND) }
                return@collectLatest
            }

            val resource = teachersDataSource.getTeachers(
                year = configuration.year,
                semesterId = configuration.semesterId
            )

            logger.d(TAG, "getTeachers resource: $resource")
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorStatus = resource.status.toErrorStatus(),
                    teachers = resource.payload?.toImmutableList() ?: persistentListOf(),
                )
            }
        }
    }

    /**
     * Selects a teacher title filter by updating the UI state with the selected filter ID.
     * This allows the UI to filter the displayed teachers based on their titles.
     * @param teacherTitleFilterId The ID of the selected teacher title filter.
     */
    fun selectTeacherTitleFilter(teacherTitleFilterId: String) {
        logger.d(TAG, "selectTeacherTitleFilter: $teacherTitleFilterId")
        _uiState.update {
            it.copy(selectedFilterId = teacherTitleFilterId)
        }
    }

    /**
     * Sets the search query for filtering teachers.
     * This updates the UI state with the new search query, which will be used to
     * filter the teachers list.
     * @param searchQuery The new search query to set.
     */
    fun setSearchQuery(searchQuery: String) {
        logger.d(TAG, "setSearchQuery: $searchQuery")
        _uiState.update {
            it.copy(searchQuery = searchQuery)
        }
    }

    /**
     * Retries the data fetch operation for teachers by cancelling the current job
     * and starting a new one. This is useful in case of an error or when the user
     * wants to refresh the teacher list.
     */
    fun retry() {
        logger.d(TAG, "retry")
        job.cancel()
        job = getTeachers()
    }

    companion object {
        private const val TAG = "TeachersViewModel"
    }
}
