package com.ubb.fmi.orar.feature.form.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.timetable.model.StudyLevel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.GroupsFromUiState
import com.ubb.fmi.orar.ui.catalog.model.UserType
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.groups.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.domain.timetable.usecase.SetConfigurationUseCase
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
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
import kotlin.collections.firstOrNull
import kotlin.time.Duration.Companion.seconds

class GroupsFormViewModel(
    private val year: Int,
    private val semesterId: String,
    private val fieldId: String,
    private val studyLevelId: String,
    private val degreeId: String,
    private val studyLinesDataSource: StudyLinesDataSource,
    private val timetablePreferences: TimetablePreferences,
    private val setConfigurationUseCase: SetConfigurationUseCase,
) : EventViewModel<GroupsFromUiState.GroupsFromEvent>() {

    private val _uiState = MutableStateFlow(GroupsFromUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { getGroups() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getGroups() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, isError = false) }

        val studyLevel = StudyLevel.getById(studyLevelId)
        val lineId = fieldId + studyLevel.notation

        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        val studyLinesResource = studyLinesDataSource.getOwners(
            year = year,
            semesterId = semesterId,
        )

        val studyLine = studyLinesResource.payload?.firstOrNull { it.id == lineId }
        val groupsResource = studyLinesDataSource.getGroups(
            year = year,
            semesterId = semesterId,
            ownerId = lineId
        )

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = groupsResource.status.isError(),
                groups = groupsResource.payload?.toImmutableList() ?: persistentListOf(),
                title = studyLine?.name,
                subtitle = studyLevel.label,
                selectedGroupId = groupsResource.payload?.firstOrNull { groupId ->
                    groupId == configuration?.groupId
                }
            )
        }
    }

    fun selectGroup(groupId: String) {
        _uiState.update { it.copy(selectedGroupId = groupId) }
    }

    fun retry() {
        getGroups()
    }

    fun finishSelection() {
        viewModelScope.launch {
            _uiState.value.selectedGroupId?.let { groupId ->
                setConfigurationUseCase(
                    year = year,
                    semesterId = semesterId,
                    userTypeId = UserType.STUDENT.id,
                    fieldId = fieldId,
                    studyLevelId = studyLevelId,
                    studyLineDegreeId = degreeId,
                    groupId = groupId,
                    teacherId = null
                )

                registerEvent(GroupsFromUiState.GroupsFromEvent.CONFIGURATION_DONE)
            }
        }
    }
}
