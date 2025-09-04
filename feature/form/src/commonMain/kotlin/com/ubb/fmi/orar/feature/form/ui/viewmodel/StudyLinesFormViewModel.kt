package com.ubb.fmi.orar.feature.form.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.ui.catalog.model.DegreeFilter
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
 * ViewModel for managing the study lines form state in the application.
 * This ViewModel fetches study lines based on the provided year and semester ID,
 * and allows the user to select a field, study level, and degree filter.
 *
 * @param year The academic year for which study lines are being fetched.
 * @param semesterId The ID of the semester for which study lines are being fetched.
 * @param studyLinesDataSource The data source for fetching study lines.
 * @param timetablePreferences Preferences for managing timetable configurations.
 */
class StudyLinesFormViewModel(
    private val year: Int,
    private val semesterId: String,
    private val studyLinesDataSource: StudyLinesDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    /**
     * Mutable state flow that holds the UI state for the study lines form.
     * It is initialized with a default state and will be updated as data is fetched.
     */
    private val _uiState = MutableStateFlow(StudyLinesFormUiState())
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
        _uiState.update { it.copy(isLoading = true, isError = false) }
        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        val studyLinesResource = studyLinesDataSource.getOwners(
            year = year,
            semesterId = semesterId
        )

        val studyLevel = configuration?.studyLevelId?.let(StudyLevel::getById)
        val lineId = studyLevel?.let { configuration.fieldId + studyLevel.notation }
        val selectedStudyLine = studyLinesResource.payload?.firstOrNull { it.id == lineId }
        val groupedStudyLines = studyLinesResource.payload?.groupBy { studyLine ->
            studyLine.fieldId
        }?.values?.toList()?.map { studyLines ->
            studyLines.sortedBy { it.levelId }.toImmutableList()
        }?.toImmutableList() ?: persistentListOf()

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = studyLinesResource.status.isError(),
                groupedStudyLines = groupedStudyLines,
                selectedFilterId = selectedStudyLine?.degreeId ?: DegreeFilter.ALL.id,
                selectedFieldId = selectedStudyLine?.fieldId,
                selectedStudyLevelId = selectedStudyLine?.levelId,
            )
        }
    }

    /**
     * Selects a field by its ID and updates the UI state accordingly.
     * This method clears the selected study level ID when a new field is selected.
     * @param fieldId The ID of the field to be selected.
     */
    fun selectFieldId(fieldId: String) {
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
        _uiState.update { it.copy(selectedStudyLevelId = studyLevel) }
    }

    /**
     * Selects a degree filter by its ID and updates the UI state accordingly.
     * This method clears the selected field ID and study level ID when a new degree filter is selected.
     * @param degreeFilterId The ID of the degree filter to be selected.
     */
    fun selectDegreeFilter(degreeFilterId: String) {
        _uiState.update {
            it.copy(
                selectedFilterId = degreeFilterId,
                selectedFieldId = null,
                selectedStudyLevelId = null,
            )
        }
    }

    /**
     * Finishes the selection process by saving the selected configuration to preferences.
     * This method is called when the user confirms their selection.
     */
    fun retry() {
        getStudyLines()
    }
}
