package com.ubb.fmi.orar.feature.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.core.model.UserType
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.feature.app.viewmodel.model.AppUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class AppViewModel(
    private val timetablePreferences: TimetablePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()
        .onStart { checkConfiguration() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _uiState.value
        )

    private fun checkConfiguration() {
        viewModelScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) {
                    _uiState.update { it.copy(isConfigurationDone = false) }
                    return@collectLatest
                }

                _uiState.update {
                    val hasDegree = configuration.degreeId != null

                    val hasStudyLineBase = configuration.studyLineBaseId != null
                    val hasStudyLineYear = configuration.studyLineYearId != null
                    val hasStudyLine = hasStudyLineBase && hasStudyLineYear

                    val hasStudyGroup = configuration.groupId != null
                    val hasStudyGroupType = configuration.groupTypeId != null
                    val hasStudyGroupInfo = hasStudyGroup && hasStudyGroupType

                    val hasStudyLineInfo = hasStudyLine && hasStudyGroupInfo

                    val isStudent = configuration.userTypeId == UserType.STUDENT.id
                    val isTeacher = configuration.userTypeId == UserType.TEACHER.id

                    val hasStudentInfo = isStudent && hasDegree && hasStudyLineInfo
                    val hasTeacherInfo = isTeacher && configuration.teacherId != null

                    val hasUserInfo = hasStudentInfo || hasTeacherInfo

                    it.copy(isConfigurationDone = hasUserInfo)
                }
            }
        }
    }
}
