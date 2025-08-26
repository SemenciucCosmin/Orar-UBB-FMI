package com.ubb.fmi.orar.feature.form.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState.Companion.filteredGroupedStudyLines
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.DegreeFilter
import com.ubb.fmi.orar.network.model.isError
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class StudyLinesFormViewModel(
    private val studyLinesDataSource: StudyLineDataSource,
    private val timetablePreferences: TimetablePreferences
) : EventViewModel<StudyLinesFormUiState.StudyLinesFormEvent>() {

    private val _uiState = MutableStateFlow(StudyLinesFormUiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    init {
        getStudyLines()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getStudyLines() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, isError = false) }

        val configuration = timetablePreferences.getConfiguration().firstOrNull() ?: return@launch
        val studyLinesResource = studyLinesDataSource.getStudyLines(
            year = configuration.year,
            semesterId = configuration.semesterId
        )

        val groupedStudyLines = studyLinesResource.payload?.groupBy { studyLine ->
            studyLine.baseId
        }?.values?.toList()?.map { studyLines ->
            studyLines.sortedBy { it.studyYearId }.toImmutableList()
        }?.toImmutableList() ?: persistentListOf()

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = studyLinesResource.status.isError(),
                groupedStudyLines = groupedStudyLines
            )
        }
    }

    fun selectStudyLineBaseId(studyLineBaseId: String) {
        _uiState.update {
            it.copy(
                selectedStudyLineBaseId = studyLineBaseId,
                selectedStudyYearId = null,
            )
        }
    }

    fun selectStudyYear(studyYear: String) {
        _uiState.update { it.copy(selectedStudyYearId = studyYear) }
    }


    fun selectDegreeFilter(degreeFilter: DegreeFilter) {
        _uiState.update {
            it.copy(
                selectedFilter = degreeFilter,
                selectedStudyLineBaseId = null,
                selectedStudyYearId = null,
            )
        }
    }

    fun retry() {
        getStudyLines()
    }

    fun finishSelection() {
        viewModelScope.launch {
            val studyLineBaseId = _uiState.value.selectedStudyLineBaseId
            val studyYearId = _uiState.value.selectedStudyYearId
            val selectedStudyLine = _uiState.value.filteredGroupedStudyLines
                .flatten()
                .firstOrNull { it.baseId == studyLineBaseId && it.studyYearId == studyYearId }

            if (selectedStudyLine != null) {
                timetablePreferences.setStudyLineBaseId(selectedStudyLine.baseId)
                timetablePreferences.setStudyLineYearId(selectedStudyLine.studyYearId)
                timetablePreferences.setDegreeId(selectedStudyLine.degreeId)
                registerEvent(StudyLinesFormUiState.StudyLinesFormEvent.SELECTION_DONE)
            }
        }
    }
}
