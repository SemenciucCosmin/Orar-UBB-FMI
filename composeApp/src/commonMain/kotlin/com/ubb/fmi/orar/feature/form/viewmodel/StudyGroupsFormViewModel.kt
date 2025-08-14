package com.ubb.fmi.orar.feature.form.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.core.model.StudyYear
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.feature.form.viewmodel.model.StudyGroupsFromUiState
import com.ubb.fmi.orar.feature.form.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status
import com.ubb.fmi.orar.network.model.isError
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
import kotlinx.serialization.json.Json.Default.configuration
import kotlin.time.Duration.Companion.seconds

class StudyGroupsFormViewModel(
    private val studyLineaDataSource: StudyLineDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private var job: Job
    private val _uiState = MutableStateFlow(StudyGroupsFromUiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    init {
        job = getStudyGroups()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getStudyGroups() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        timetablePreferences.getConfiguration().filterNotNull().transformLatest { configuration ->
            val studyYear = configuration.studyLineYearId?.let(StudyYear::getById)
            val studyLineId = configuration.studyLineBaseId + studyYear?.notation

            val timetablesResource = studyLineaDataSource.getTimetables(
                year = configuration.year,
                semesterId = configuration.semesterId
            )

            val studyGroups = timetablesResource.payload?.firstOrNull { studyLineTimetable ->
                studyLineTimetable.studyLine.id == studyLineId
            }?.classes?.map { it.groupId }?.distinct()

            emit(Resource(studyGroups, timetablesResource.status))
        }.collectLatest { studyLinesResource ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = studyLinesResource.status.isError(),
                    studyGroups = studyLinesResource.payload ?: emptyList()
                )
            }
        }
    }

    fun selectStudyGroup(studyGroup: String) {
        _uiState.update { it.copy(selectedStudyGroupId = studyGroup) }
    }

    fun retry() {
        job.cancel()
        job = getStudyGroups()
    }

    fun finishSelection() {
        viewModelScope.launch {
            _uiState.value.selectedStudyGroupId?.let { studyGroupId ->
                timetablePreferences.setGroupId(studyGroupId)
            }
        }
    }
}
