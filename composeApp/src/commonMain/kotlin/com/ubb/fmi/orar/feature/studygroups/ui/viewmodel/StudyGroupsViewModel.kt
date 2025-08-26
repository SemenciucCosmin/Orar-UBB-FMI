package com.ubb.fmi.orar.feature.studygroups.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.feature.studygroups.ui.viewmodel.model.StudyGroupsUiState
import com.ubb.fmi.orar.network.model.isError
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

class StudyGroupsViewModel(
    private val studyLineId: String,
    private val studyLinesDataSource: StudyLineDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudyGroupsUiState())
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

        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        val studyGroupsResource = studyLinesDataSource.getStudyGroupsIds(
            year = configuration?.year ?: return@launch,
            semesterId = configuration.semesterId,
            studyLineId = studyLineId,
        )

        _uiState.update {
            it.copy(
                isLoading = false,
                isError = studyGroupsResource.status.isError(),
                studyGroups = studyGroupsResource.payload?.toImmutableList()  ?: persistentListOf()
            )
        }
    }

    fun retry() {
        getStudyGroups()
    }
}
