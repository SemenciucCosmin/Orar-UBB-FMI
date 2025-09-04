package com.ubb.fmi.orar.feature.startup.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.timetable.usecase.CheckCachedDataValidityUseCase
import com.ubb.fmi.orar.feature.startup.ui.viewmodel.model.StartupEvent
import com.ubb.fmi.orar.ui.catalog.model.UserType
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for handling the startup logic of the application.
 * It checks the configuration and cached data validity to determine the next steps.
 *
 * @property timetablePreferences Preferences for managing timetable configurations.
 * @property checkCachedDataValidityUseCase Use case to check if cached data is still valid.
 */
class StartupViewModel(
    private val timetablePreferences: TimetablePreferences,
    private val checkCachedDataValidityUseCase: CheckCachedDataValidityUseCase
) : EventViewModel<StartupEvent>() {

    /**
     * Initializes the ViewModel and checks the configuration validity.
     * This is called when the ViewModel is created.
     */
    init {
        checkConfiguration()
    }

    /**
     * Checks the current configuration and cached data validity.
     * Depending on the results, it emits appropriate events to indicate
     * whether the configuration is complete or incomplete.
     */
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
