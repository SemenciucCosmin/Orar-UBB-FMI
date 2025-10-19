package com.ubb.fmi.orar.feature.grouptimetable.ui.viewmodel

import Logger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.groups.repository.GroupsRepository
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.usertimetable.model.Week
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetCurrentWeekUseCase
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.TimetableUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for the Study Line Timetable feature.
 *
 * This ViewModel is responsible for loading and managing the timetable data for a specific study line,
 * including handling loading states, errors, and user interactions such as frequency selection.
 */
class GroupTimetableViewModel(
    private val fieldId: String,
    private val studyLevelId: String,
    private val groupId: String,
    private val groupsRepository: GroupsRepository,
    private val getCurrentWeekUseCase: GetCurrentWeekUseCase,
    private val logger: Logger,
) : ViewModel() {

    /**
     * Mutable state flow representing the UI state of the timetable.
     * It holds information about loading status, error status, classes, title, study level, group,
     * and the selected frequency.
     */
    private val _uiState = MutableStateFlow(TimetableUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()
        .onStart {
            getWeek()
            loadTimetable()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    /**
     * Initializes the ViewModel and starts loading the timetable data.
     * The timetable is loaded asynchronously, and the UI state is updated accordingly.
     */
    private fun loadTimetable() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorStatus = null) }

            val studyLevel = StudyLevel.getById(studyLevelId)
            val studyLineId = fieldId + studyLevel.notation

            logger.d(TAG, "loadTimetable studyLevel: $studyLevel")
            logger.d(TAG, "loadTimetable fieldId: $fieldId")
            logger.d(TAG, "loadTimetable groupId: $groupId")

            groupsRepository.getTimetable(groupId, studyLineId).collectLatest { resource ->
                logger.d(TAG, "loadTimetable resource: $resource")
                val events = resource.payload?.events?.map {
                    it.copy(isVisible = true)
                }?.toImmutableList() ?: persistentListOf()

                _uiState.update {
                    it.copy(
                        isLoading = resource.status.isLoading(),
                        errorStatus = resource.status.toErrorStatus(),
                        events = events,
                        title = resource.payload?.owner?.name ?: String.BLANK,
                        studyLevel = studyLevel,
                        group = groupId,
                    )
                }
            }
        }
    }

    /**
     * Retrieves the current week for proper timetable filtering
     */
    private fun getWeek() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }
        getCurrentWeekUseCase().collectLatest { week ->
            val frequency = when (week) {
                Week.ODD -> Frequency.WEEK_1
                Week.EVEN -> Frequency.WEEK_2
            }

            selectFrequency(frequency)
        }
    }

    /**
     * Selects a frequency for the timetable.
     * This updates the UI state with the selected frequency.
     *
     * @param frequency The frequency to be selected.
     */
    fun selectFrequency(frequency: Frequency) {
        logger.d(TAG, "selectFrequency: $frequency")
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    /**
     * Retries loading the timetable data.
     * This is typically called when an error occurs and the user wants to try loading the data again.
     */
    fun retry() {
        logger.d(TAG, "retry")
        loadTimetable()
    }

    companion object {
        private const val TAG = "StudyLineTimetableViewModel"
    }
}
