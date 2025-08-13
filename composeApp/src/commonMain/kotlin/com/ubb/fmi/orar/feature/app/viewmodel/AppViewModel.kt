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
                _uiState.update {
                    val isStudyYearSelected = configuration?.year != null
                    val isSemesterSelected = configuration?.semesterId != null
                    val isDegreeSelected = configuration?.degreeId != null
                    val isStudyLineBaseIdSelected = configuration?.studyLineBaseId != null
                    val isStudyLineYearIdSelected = configuration?.studyLineYearId != null
                    val isStudyLineSelected = isStudyLineBaseIdSelected && isStudyLineYearIdSelected
                    val isStudyGroupSelected = configuration?.groupId != null
                    val isTeacherSelected = configuration?.teacherId != null
                    val isStudentTypeSelected = configuration?.userTypeId == UserType.STUDENT.id
                    val isTeacherTypeSelected = configuration?.userTypeId == UserType.TEACHER.id

                    val isStudyLineInfoSelected = isStudyLineSelected && isStudyGroupSelected
                    val isStudentInfoSelected = isStudentTypeSelected &&
                            isDegreeSelected &&
                            isStudyLineInfoSelected

                    val isTeacherInfoSelected = isTeacherTypeSelected && isTeacherSelected
                    val isUserInfoSelected = isStudentInfoSelected || isTeacherInfoSelected
                    val isStudyInfoSelected = isStudyYearSelected && isSemesterSelected

                    it.copy(isConfigurationDone = isStudyInfoSelected && isUserInfoSelected)
                }
            }
        }
    }
}
