package com.ubb.fmi.orar.feature.form.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.DegreeFilter
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.groups.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.domain.timetable.model.StudyLevel
import com.ubb.fmi.orar.feature.form.ui.model.Semester
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

class StudyLinesFormViewModel(
    private val year: Int,
    private val semesterId: String,
    private val studyLinesDataSource: StudyLinesDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudyLinesFormUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { getStudyLines()}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getStudyLines() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, isError = false) }
        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        if (configuration == null) {
            _uiState.update { it.copy(isLoading = false, isError = true) }
            return@launch
        }

        val semester = Semester.getById(semesterId)
        val studyLinesResource = studyLinesDataSource.getOwners(
            year = year,
            semesterId = semesterId
        )

        val studyLevel = configuration.studyLevelId?.let(StudyLevel::getById)
        val lineId = studyLevel?.let { configuration.fieldId + studyLevel.notation}
        val selectedStudyLine = studyLinesResource.payload?.firstOrNull{ it.id == lineId }
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
                selectedFilter = DegreeFilter.getById(selectedStudyLine?.degreeId),
                selectedFieldId = selectedStudyLine?.fieldId,
                selectedStudyLevelId = selectedStudyLine?.levelId,
                title = "$year - ${year.inc()}, ${semester.label}",
            )
        }
    }

    fun selectFieldId(fieldId: String) {
        _uiState.update {
            it.copy(
                selectedFieldId = fieldId,
                selectedStudyLevelId = null,
            )
        }
    }

    fun selectStudyLevel(studyLevel: String) {
        _uiState.update { it.copy(selectedStudyLevelId = studyLevel) }
    }


    fun selectDegreeFilter(degreeFilter: DegreeFilter) {
        _uiState.update {
            it.copy(
                selectedFilter = degreeFilter,
                selectedFieldId = null,
                selectedStudyLevelId = null,
            )
        }
    }

    fun retry() {
        getStudyLines()
    }
}
