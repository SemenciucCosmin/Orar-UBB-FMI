package com.ubb.fmi.orar.feature.startup.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.timetable.usecase.CheckCachedDataValidityUseCase
import com.ubb.fmi.orar.feature.startup.ui.viewmodel.model.StartupEvent
import com.ubb.fmi.orar.ui.catalog.model.UserType
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class StartupViewModel(
    private val timetablePreferences: TimetablePreferences,
    private val checkCachedDataValidityUseCase: CheckCachedDataValidityUseCase
) : EventViewModel<StartupEvent>() {

    init {
        checkConfiguration()
    }

    private fun checkConfiguration() {
        viewModelScope.launch {
            checkCachedDataValidityUseCase()

            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            val hasDegree = configuration?.degreeId != null

            val hasStudyLineBase = configuration?.fieldId != null
            val hasStudyLineYear = configuration?.studyLevelId != null
            val hasStudyLine = hasStudyLineBase && hasStudyLineYear

            val hasGroup = configuration?.groupId != null
            val hasStudyLineInfo = hasStudyLine && hasGroup

            val isStudent = configuration?.userTypeId == UserType.STUDENT.id
            val isTeacher = configuration?.userTypeId == UserType.TEACHER.id

            val hasStudentInfo = isStudent && hasDegree && hasStudyLineInfo
            val hasTeacherInfo = isTeacher && configuration.teacherId != null

            val hasUserInfo = hasStudentInfo || hasTeacherInfo

            when {
                hasUserInfo -> registerEvent(StartupEvent.CONFIGURATION_COMPLETE)
                else -> registerEvent(StartupEvent.CONFIGURATION_INCOMPLETE)
            }
        }
    }
}
