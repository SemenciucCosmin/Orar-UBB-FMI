package com.ubb.fmi.orar.feature.form.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.network.model.isError
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.ui.catalog.model.StudyLevel
import com.ubb.fmi.orar.domain.timetable.usecase.SetTimetableConfigurationUseCase
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.GroupsFromUiState
import com.ubb.fmi.orar.ui.catalog.model.UserType
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

/**
 * ViewModel for managing the selection of groups in a timetable form.
 * This ViewModel fetches groups based on the provided parameters and allows the user to
 * select a group. It also handles the saving of the selected group configuration
 * and provides a way to retry fetching groups in case of an error.
 * @param year: The academic year for which groups are being fetched.
 * @param semesterId: The ID of the semester for which groups are being fetched.
 * @param fieldId: The ID of the field of study.
 * @param studyLevelId: The ID of the study level (e.g., Bachelor, Master).
 * @param degreeId: The ID of the degree.
 * @param studyLinesDataSource: The data source for fetching study lines and groups.
 * @param timetablePreferences: Preferences for the timetable configuration.
 * @param setTimetableConfigurationUseCase: Use case for setting the timetable configuration
 */
class GroupsFormViewModel(
    private val year: Int,
    private val semesterId: String,
    private val fieldId: String,
    private val studyLevelId: String,
    private val degreeId: String,
    private val studyLinesDataSource: StudyLinesDataSource,
    private val timetablePreferences: TimetablePreferences,
    private val setTimetableConfigurationUseCase: SetTimetableConfigurationUseCase,
) : EventViewModel<GroupsFromUiState.GroupsFromEvent>() {

    /**
     * Mutable state flow that holds the UI state for the groups selection.
     * It is initialized with a default state and will be updated as data is fetched.
     */
    private val _uiState = MutableStateFlow(GroupsFromUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { getGroups() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    /**
     * Initializes the ViewModel and starts fetching groups.
     * This is done in the init block to ensure that groups are fetched as soon as the ViewModel is created.
     */
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
                studyLevel = studyLevel,
                selectedGroupId = groupsResource.payload?.firstOrNull { groupId ->
                    groupId == configuration?.groupId
                }
            )
        }
    }

    /**
     * Selects a group by its ID and updates the UI state.
     * This function is called when the user selects a group from the list.
     * @param groupId: The ID of the group to select.
     */
    fun selectGroup(groupId: String) {
        _uiState.update { it.copy(selectedGroupId = groupId) }
    }

    /**
     * Retries fetching groups in case of an error.
     * This function is called when the user wants to retry fetching groups after an error occurs.
     */
    fun retry() {
        getGroups()
    }

    /**
     * Finishes the group selection process and saves the selected group configuration.
     * This function is called when the user confirms their selection.
     */
    fun finishSelection() {
        viewModelScope.launch {
            _uiState.value.selectedGroupId?.let { groupId ->
                setTimetableConfigurationUseCase(
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
