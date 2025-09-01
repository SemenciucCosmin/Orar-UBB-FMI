package com.ubb.fmi.orar.feature.studylines.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.StudyLinesUiState
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.groups.datasource.StudyLinesDataSource
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudyLinesViewModel(
    private val studyLinesDataSource: StudyLinesDataSource,
    private val timetablePreferences: TimetablePreferences,
) : ViewModel() {

    private var job: Job
    private val _uiState = MutableStateFlow(StudyLinesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        job = getStudyLines()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getStudyLines() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        timetablePreferences.getConfiguration().collectLatest { configuration ->
            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
                return@collectLatest
            }

            val studyLinesResource = studyLinesDataSource.getOwners(
                year = configuration.year,
                semesterId = configuration.semesterId
            )

            val groupedStudyLines = studyLinesResource.payload?.groupBy { studyLine ->
                studyLine.fieldId
            }?.values?.toList()?.map { studyLines ->
                studyLines.sortedBy { it.levelId }.toImmutableList()
            }?.toImmutableList() ?: persistentListOf()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = studyLinesResource.status.isError(),
                    groupedStudyLines = groupedStudyLines
                )
            }
        }
    }

    fun selectFieldId(fieldId: String) {
        _uiState.update {
            it.copy(selectedFieldId = fieldId)
        }
    }

    fun selectDegreeFilter(degreeFilterId: String) {
        _uiState.update {
            it.copy(
                selectedFilterId = degreeFilterId,
                selectedFieldId = null,
            )
        }
    }

    fun retry() {
        job.cancel()
        job = getStudyLines()
    }
}