package com.ubb.fmi.orar.feature.form.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.domain.usertimetable.model.StudyYear
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyGroupsFromUiState
import com.ubb.fmi.orar.network.model.isError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class StudyGroupsFormViewModel(
    private val studyLinesDataSource: StudyLineDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudyGroupsFromUiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    init {
        getStudyGroups()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getStudyGroups() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, isError = false) }

        val configuration = timetablePreferences.getConfiguration().firstOrNull() ?: return@launch
        val studyYear = configuration.studyLineYearId?.let(StudyYear::getById)
        val studyLineId = configuration.studyLineBaseId + studyYear?.notation

        val studyGroupsResource = studyLinesDataSource.getStudyGroupsIds(
            year = configuration.year,
            semesterId = configuration.semesterId,
            studyLineId = studyLineId,
        )

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = studyGroupsResource.status.isError(),
                studyGroups = studyGroupsResource.payload ?: emptyList(),
            )
        }
    }

    fun selectStudyGroup(studyGroupId: String) {
        _uiState.update { it.copy(selectedStudyGroupId = studyGroupId) }
    }

    fun retry() {
        getStudyGroups()
    }

    fun finishSelection() {
        viewModelScope.launch {
            _uiState.value.selectedStudyGroupId?.let { studyGroupId ->
                timetablePreferences.setGroupId(studyGroupId)
            }
        }
    }
}
