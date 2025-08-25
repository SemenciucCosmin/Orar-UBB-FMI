package com.ubb.fmi.orar.feature.studylinetimetable.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.core.model.Frequency
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.feature.studylinetimetable.ui.viewmodel.model.StudyLineTimetableUiState
import com.ubb.fmi.orar.network.model.isError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class StudyLineTimetableViewModel(
    private val studyLineId: String,
    private val studyGroupId: String,
    private val studyLineDataSource: StudyLineDataSource,
    private val roomsDataSource: RoomsDataSource,
    private val subjectsDataSource: SubjectsDataSource,
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudyLineTimetableUiState())
    val uiState = _uiState.asStateFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    init {
        loadTimetable()
    }

    private fun loadTimetable() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            if (configuration == null) {
                _uiState.update { it.copy(isLoading = false, isError = true) }
                return@launch
            }

            val timetableResource = studyLineDataSource.getTimetable(
                year = configuration.year,
                semesterId = configuration.semesterId,
                studyLineId = studyLineId
            )

            val groupClasses = timetableResource.payload?.classes?.filter {
                it.groupId == studyGroupId
            } ?: emptyList()

            val teachersResource = teachersDataSource.getTeachers(
                year = configuration.year,
                semesterId = configuration.semesterId,
            )

            val subjectsResource = subjectsDataSource.getSubjects(
                year = configuration.year,
                semesterId = configuration.semesterId,
            )

            val roomsResource = roomsDataSource.getRooms(
                year = configuration.year,
                semesterId = configuration.semesterId,
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isError = timetableResource.status.isError(),
                    classes = groupClasses,
                    teachers = teachersResource.payload ?: emptyList(),
                    subjects = subjectsResource.payload ?: emptyList(),
                    rooms = roomsResource.payload ?: emptyList(),
                    studyLine = timetableResource.payload?.studyLine
                )
            }
        }
    }

    fun selectFrequency(frequency: Frequency) {
        _uiState.update { it.copy(selectedFrequency = frequency) }
    }

    fun retry() {
        loadTimetable()
    }
}
