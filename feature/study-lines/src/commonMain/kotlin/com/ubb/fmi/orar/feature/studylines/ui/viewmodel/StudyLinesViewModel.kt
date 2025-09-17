package com.ubb.fmi.orar.feature.studylines.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.StudyLinesUiState
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Study Lines feature.
 * It fetches study lines from the data source and manages the UI state.
 * The ViewModel handles loading, error states, and user interactions such as selecting filters and fields.
 *
 * @property studyLinesDataSource The data source for fetching study lines.
 * @property timetablePreferences Preferences for the timetable configuration.
 */
class StudyLinesViewModel(
    private val studyLinesDataSource: StudyLinesDataSource,
    private val timetablePreferences: TimetablePreferences,
    private val logger: Logger,
) : ViewModel() {

    /**
     * Job to manage the coroutine for fetching study lines.
     * This allows for cancellation and restarting of the data fetching process.
     */
    private var job: Job

    /**
     * Mutable state flow holding the UI state for the Study Lines screen.
     * It contains information about loading status, error status, grouped study lines,
     * and selected filters and fields.
     */
    private val _uiState = MutableStateFlow(StudyLinesUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Initializes the ViewModel by starting the coroutine to fetch study lines.
     * This is done in the init block to ensure it starts as soon as the ViewModel is created.
     */
    init {
        job = getStudyLines()
    }

    /**
     * Coroutine to fetch study lines from the data source.
     * It updates the UI state with loading, error, and fetched data.
     * The coroutine collects the timetable configuration and uses it to fetch study lines.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getStudyLines() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        timetablePreferences.getConfiguration().collectLatest { configuration ->
            logger.d(TAG, "getStudyLines configuration: $configuration")
            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, errorStatus = null) }
                return@collectLatest
            }

            val studyLinesResource = studyLinesDataSource.getOwners(
                year = configuration.year,
                semesterId = configuration.semesterId
            )

            logger.d(TAG, "getStudyLines studyLinesResource: $studyLinesResource")

            val groupedStudyLines = studyLinesResource.payload?.groupBy { studyLine ->
                studyLine.fieldId
            }?.values?.toList()?.map { studyLines ->
                studyLines.sortedBy { it.levelId }.toImmutableList()
            }?.toImmutableList() ?: persistentListOf()

            logger.d(TAG, "getStudyLines groupedStudyLines: $groupedStudyLines")
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorStatus = studyLinesResource.status.toErrorStatus(),
                    groupedStudyLines = groupedStudyLines
                )
            }
        }
    }

    /**
     * Selects a field by its ID and updates the UI state.
     * This allows the user to focus on a specific field of study.
     *
     * @param fieldId The ID of the field to select.
     */
    fun selectFieldId(fieldId: String) {
        logger.d(TAG, "selectFieldId: $fieldId")
        _uiState.update {
            it.copy(selectedFieldId = fieldId)
        }
    }

    /**
     * Selects a degree filter by its ID and updates the UI state.
     * This allows the user to filter study lines based on the selected degree.
     *
     * @param degreeFilterId The ID of the degree filter to select.
     */
    fun selectDegreeFilter(degreeFilterId: String) {
        logger.d(TAG, "selectDegreeFilter: $degreeFilterId")
        _uiState.update {
            it.copy(
                selectedFilterId = degreeFilterId,
                selectedFieldId = null,
            )
        }
    }

    /**
     * Retries fetching study lines by canceling the current job and starting a new one.
     * This is useful when the user wants to refresh the data after an error.
     */
    fun retry() {
        logger.d(TAG, "retry")
        job.cancel()
        job = getStudyLines()
    }

    companion object {
        private const val TAG = "StudyLinesViewModel"
    }
}