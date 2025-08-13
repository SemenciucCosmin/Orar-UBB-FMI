package com.ubb.fmi.orar.feature.form.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.feature.form.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.isError
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class StudyLinesFormViewModel(
    private val studyLineaDataSource: StudyLineDataSource,
    private val timetablePreferences: TimetablePreferences
) : EventViewModel<StudyLinesFormUiState.StudyLinesFormEvent>() {

    private var job: Job
    private val _uiState = MutableStateFlow(StudyLinesFormUiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    init {
        job = getStudyLines()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getStudyLines() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        timetablePreferences.getConfiguration().filterNotNull().transformLatest { configuration ->
            val studyLinesResource = studyLineaDataSource.getStudyLines(
                year = configuration.year,
                semesterId = configuration.semesterId
            )

            val filteredStudyLines = studyLinesResource.payload?.filter { studyLine ->
                studyLine.degreeId == configuration.degreeId
            }

            emit(Resource(filteredStudyLines, studyLinesResource.status))
        }.collectLatest { studyLinesResource ->
            val studyLinesGroups = studyLinesResource.payload?.groupBy { studyLine ->
                studyLine.baseId
            }?.values?.toList() ?: emptyList()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = studyLinesResource.status.isError(),
                    studyLinesGroups = studyLinesGroups
                )
            }
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

    fun retry() {
        job.cancel()
        job = getStudyLines()
    }

    fun finishSelection() {
        viewModelScope.launch {
            val studyLineBaseId = _uiState.value.selectedStudyLineBaseId
            val studyYearId = _uiState.value.selectedStudyYearId

            if (studyLineBaseId != null && studyYearId != null) {
                timetablePreferences.setStudyLineBaseId(studyLineBaseId)
                timetablePreferences.setStudyLineYearId(studyYearId)
                registerEvent(StudyLinesFormUiState.StudyLinesFormEvent.SELECTION_DONE)
            }
        }
    }
}
