package com.ubb.fmi.orar.feature.form.ui.viewmodel

import Logger
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.timetable.usecase.SetTimetableConfigurationUseCase
import com.ubb.fmi.orar.domain.usertimetable.model.UserType
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.TeachersFormUiState
import com.ubb.fmi.orar.ui.catalog.extensions.toErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.TeacherTitleFilter
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * ViewModel for managing the selection of teachers in a timetable form.
 * This ViewModel fetches teachers based on the provided year and semester ID,
 * and allows the user to select a teacher and filter by title.
 *
 * @param year The academic year for which teachers are being fetched.
 * @param semesterId The ID of the semester for which teachers are being fetched.
 * @param teachersDataSource The data source for fetching teachers.
 * @param timetablePreferences Preferences for managing timetable configurations.
 * @param setTimetableConfigurationUseCase Use case for setting the timetable configuration.
 */
class TeachersFormViewModel(
    private val year: Int,
    private val semesterId: String,
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences,
    private val setTimetableConfigurationUseCase: SetTimetableConfigurationUseCase,
    private val logger: Logger,
) : EventViewModel<TeachersFormUiState.TeachersFormUiEvent>() {

    /**
     * Mutable state flow that holds the UI state for the teachers selection.
     * It is initialized with a default state and will be updated as data is fetched.
     */
    private val _uiState = MutableStateFlow(TeachersFormUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { getTeachers() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    /**
     * Initializes the ViewModel by fetching the teachers based on the provided year and semester ID.
     * This method updates the UI state with the fetched teachers, and sets the selected teacher
     * and filter based on the current configuration.
     */
    fun selectTeacherTitleFilter(teacherTitleFilterId: String) {
        logger.d(TAG, "selectTeacherTitleFilter: $teacherTitleFilterId")
        _uiState.update {
            it.copy(selectedFilterId = teacherTitleFilterId)
        }
    }

    /**
     * Fetches the list of teachers from the data source and updates the UI state.
     * It also checks the current configuration to set the selected teacher and filter.
     */
    private fun getTeachers() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorStatus = null) }

        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        val resource = teachersDataSource.getTeachers(
            year = year,
            semesterId = semesterId
        )

        logger.d(TAG, "getTeachers for year: $year, semester: $semesterId")
        logger.d(TAG, "resource $resource")

        val teacher = resource.payload?.firstOrNull {
            it.id == configuration?.teacherId
        }

        logger.d(TAG, "teacher $teacher")

        _uiState.update {
            it.copy(
                isLoading = false,
                errorStatus = resource.status.toErrorStatus(),
                teachers = resource.payload?.toImmutableList() ?: persistentListOf(),
                selectedTeacherId = teacher?.id,
                selectedFilterId = teacher?.title?.id ?: TeacherTitleFilter.ALL.id,
            )
        }
    }

    /**
     * Selects a teacher by its ID and updates the UI state.
     * @param teacherId The ID of the teacher to be selected.
     */
    fun selectTeacher(teacherId: String) {
        logger.d(TAG, "selectTeacher $teacherId")
        _uiState.update { it.copy(selectedTeacherId = teacherId) }
    }

    /**
     * Retries fetching the teachers when an error occurs.
     * This function can be called to refresh the list of teachers.
     */
    fun retry() {
        logger.d(TAG, "retry")
        getTeachers()
    }

    /**
     * Completes the selection of a teacher and updates the timetable configuration.
     * This method is called when the user finishes selecting a teacher.
     */
    fun finishSelection() {
        logger.d(TAG, "finishSelection")
        viewModelScope.launch {
            _uiState.value.selectedTeacherId?.let { teacherId ->
                logger.d(TAG, "finishSelection teacher: $teacherId")
                setTimetableConfigurationUseCase(
                    year = year,
                    semesterId = semesterId,
                    userTypeId = UserType.TEACHER.id,
                    teacherId = teacherId,
                    fieldId = null,
                    studyLevelId = null,
                    studyLineDegreeId = null,
                    groupId = null
                )

                registerEvent(TeachersFormUiState.TeachersFormUiEvent.CONFIGURATION_DONE)
            }
        }
    }

    companion object {
        private const val TAG = "TeachersFormViewModel"
    }
}
