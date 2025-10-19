package com.ubb.fmi.orar.feature.form.ui.viewmodel

import Logger
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isEmpty
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.studylines.repository.StudyLinesRepository
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.timetable.model.Semester
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState.Companion.filteredGroupedStudyLines
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.DegreeFilter
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
 * ViewModel for managing the study lines form state in the application.
 * This ViewModel fetches study lines based on the provided year and semester ID,
 * and allows the user to select a field, study level, and degree filter.
 */
class StudyLinesFormViewModel(
    private val studyLinesRepository: StudyLinesRepository,
    private val timetablePreferences: TimetablePreferences,
    private val logger: Logger,
) : EventViewModel<StudyLinesFormUiState.StudyLinesFormUiEvent>() {

    /**
     * Mutable state flow that holds the UI state for the study lines form.
     * It is initialized with a default state and will be updated as data is fetched.
     */
    private val _uiState = MutableStateFlow(StudyLinesFormUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()
        .onStart { getStudyLines() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    /**
     * Initializes the ViewModel by fetching the study lines based on the provided year and semester ID.
     * This method updates the UI state with the fetched study lines, grouped by field ID,
     * and sets the selected study line based on the current configuration.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getStudyLines() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }

        val configuration = timetablePreferences.getConfiguration().firstOrNull()

        studyLinesRepository.getStudyLines().collectLatest { resource ->
            val studyLevel = configuration?.studyLevelId?.let(StudyLevel::getById)
            val lineId = studyLevel?.let { configuration.fieldId + studyLevel.notation }
            val selectedStudyLine = resource.payload?.firstOrNull { it.id == lineId }
            val groupedStudyLines = resource.payload?.groupBy { studyLine ->
                studyLine.fieldId
            }?.values?.toList()?.map { studyLines ->
                studyLines.sortedBy { it.level.orderIndex }.toImmutableList()
            }?.toImmutableList() ?: persistentListOf()

            logger.d(TAG, "getStudyLines resource: $resource")
            logger.d(TAG, "getStudyLines groupedStudyLines: $groupedStudyLines")
            logger.d(TAG, "getStudyLines selectedStudyLine: $selectedStudyLine")

            _uiState.update {
                it.copy(
                    year = configuration?.year,
                    semester = configuration?.semesterId?.let(Semester::getById),
                    isLoading = resource.status.isLoading(),
                    isEmpty = resource.status.isEmpty(),
                    errorStatus = resource.status.toErrorStatus(),
                    groupedStudyLines = groupedStudyLines,
                    selectedFilterId = selectedStudyLine?.degree?.id ?: DegreeFilter.ALL.id,
                    selectedFieldId = selectedStudyLine?.fieldId,
                    selectedStudyLevelId = selectedStudyLine?.level?.id,
                )
            }
        }
    }

    /**
     * Selects a field by its ID and updates the UI state accordingly.
     * This method clears the selected study level ID when a new field is selected.
     * @param fieldId The ID of the field to be selected.
     */
    fun selectFieldId(fieldId: String) {
        logger.d(TAG, "selectFieldId: $fieldId")
        _uiState.update {
            it.copy(
                selectedFieldId = fieldId,
                selectedStudyLevelId = null,
            )
        }
    }

    /**
     * Selects a study level by its ID and updates the UI state accordingly.
     * This method clears the selected field ID when a new study level is selected.
     * @param studyLevel The ID of the study level to be selected.
     */
    fun selectStudyLevel(studyLevel: String) {
        logger.d(TAG, "selectStudyLevel: $studyLevel")
        _uiState.update { it.copy(selectedStudyLevelId = studyLevel) }
    }

    /**
     * Selects a degree filter by its ID and updates the UI state accordingly.
     * This method clears the selected field ID and study level ID when a new degree filter is selected.
     * @param degreeFilterId The ID of the degree filter to be selected.
     */
    fun selectDegreeFilter(degreeFilterId: String) {
        logger.d(TAG, "selectDegreeFilter: $degreeFilterId")
        _uiState.update {
            it.copy(
                selectedFilterId = degreeFilterId,
                selectedFieldId = null,
                selectedStudyLevelId = null,
            )
        }
    }

    /**
     * Saves selection in preferences and triggers next configuration step
     */
    fun finishSelection() {
        viewModelScope.launch {
            val fieldId = uiState.value.selectedFieldId ?: return@launch
            val studyLevelId = uiState.value.selectedStudyLevelId ?: return@launch
            val studyLine = uiState.value.filteredGroupedStudyLines.flatten().firstOrNull {
                it.fieldId == fieldId && it.level.id == studyLevelId
            } ?: return@launch

            timetablePreferences.setFieldId(fieldId)
            timetablePreferences.setStudyLevelId(studyLevelId)
            timetablePreferences.setDegreeId(studyLine.degree.id)

            registerEvent(StudyLinesFormUiState.StudyLinesFormUiEvent.SELECTION_DONE)
        }
    }

    /**
     * Finishes the selection process by saving the selected configuration to preferences.
     * This method is called when the user confirms their selection.
     */
    fun retry() {
        logger.d(TAG, "retry")
        getStudyLines()
    }

    companion object {
        private const val TAG = "StudyLinesFormViewModel"
    }
}
