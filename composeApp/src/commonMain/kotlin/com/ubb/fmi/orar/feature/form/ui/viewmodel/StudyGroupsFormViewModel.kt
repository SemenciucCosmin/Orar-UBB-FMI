package com.ubb.fmi.orar.feature.form.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.domain.timetable.model.StudyYear
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyGroupsFromUiState
import com.ubb.fmi.orar.ui.catalog.model.UserType
import com.ubb.fmi.orar.data.network.model.isError
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

class StudyGroupsFormViewModel(
    private val year: Int,
    private val semesterId: String,
    private val studyLineBaseId: String,
    private val studyLineYearId: String,
    private val studyLineDegreeId: String,
    private val studyLinesDataSource: StudyLineDataSource,
    private val timetablePreferences: TimetablePreferences
) : EventViewModel<StudyGroupsFromUiState.StudyGroupsFromEvent>() {

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

        val studyLineYear = StudyYear.getById(studyLineYearId)
        val studyLineId = studyLineBaseId + studyLineYear.notation

        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        val studyGroupsResource = studyLinesDataSource.getStudyGroupsIds(
            year = year,
            semesterId = semesterId,
            studyLineId = studyLineId,
        )

        val studyGroups = studyGroupsResource.payload?.toImmutableList() ?: persistentListOf()
        val selectedStudyGroupId = studyGroups.firstOrNull { it == configuration?.groupId }

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = studyGroupsResource.status.isError(),
                studyGroups = studyGroups,
                selectedStudyGroupId = selectedStudyGroupId
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
                timetablePreferences.setYear(year)
                timetablePreferences.setSemester(semesterId)
                timetablePreferences.setStudyLineBaseId(studyLineBaseId)
                timetablePreferences.setStudyLineYearId(studyLineYearId)
                timetablePreferences.setDegreeId(studyLineDegreeId)
                timetablePreferences.setGroupId(studyGroupId)
                timetablePreferences.setUserType(UserType.STUDENT.id)
                registerEvent(StudyGroupsFromUiState.StudyGroupsFromEvent.CONFIGURATION_DONE)
            }
        }
    }
}
