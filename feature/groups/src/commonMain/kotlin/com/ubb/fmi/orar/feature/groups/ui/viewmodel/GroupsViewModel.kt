package com.ubb.fmi.orar.feature.groups.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.ui.catalog.model.StudyLevel
import com.ubb.fmi.orar.feature.groups.ui.viewmodel.model.GroupsUiState
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

class GroupsViewModel(
    private val fieldId: String,
    private val studyLevelId: String,
    private val studyLinesDataSource: StudyLinesDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupsUiState())
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
            year = configuration?.year ?: return@launch,
            semesterId = configuration.semesterId,
        )

        val studyLine = studyLinesResource.payload?.firstOrNull { it.id == lineId }
        val groupsResource = studyLinesDataSource.getGroups(
            year = configuration.year,
            semesterId = configuration.semesterId,
            ownerId = lineId
        )

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = groupsResource.status.isError(),
                groups = groupsResource.payload?.toImmutableList() ?: persistentListOf(),
                title = studyLine?.name,
                studyLevel = studyLevel
            )
        }
    }

    fun retry() {
        getGroups()
    }
}
