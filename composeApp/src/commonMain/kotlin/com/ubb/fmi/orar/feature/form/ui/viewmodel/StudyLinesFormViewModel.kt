package com.ubb.fmi.orar.feature.form.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.DegreeFilter
import com.ubb.fmi.orar.data.network.model.isError
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
    private val year: Int,
    private val semesterId: String,
    private val studyLinesDataSource: StudyLineDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

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

        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        val studyLinesResource = studyLinesDataSource.getStudyLines(
            year = year,
            semesterId = semesterId
        )

        val selectedStudyLine = studyLinesResource.payload?.firstOrNull{
            it.baseId == configuration?.studyLineBaseId
        }

        val groupedStudyLines = studyLinesResource.payload?.groupBy { studyLine ->
            studyLine.baseId
        }?.values?.toList()?.map { studyLines ->
            studyLines.sortedBy { it.studyYearId }.toImmutableList()
        }?.toImmutableList() ?: persistentListOf()

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = studyLinesResource.status.isError(),
                groupedStudyLines = groupedStudyLines,
                selectedFilter = DegreeFilter.getById(selectedStudyLine?.degreeId),
                selectedStudyLineBaseId = selectedStudyLine?.id,
                selectedStudyYearId = selectedStudyLine?.let { configuration?.studyLineYearId }
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
}
