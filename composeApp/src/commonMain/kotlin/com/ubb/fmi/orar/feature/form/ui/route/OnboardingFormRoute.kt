package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.OnboardingFormScreen
import com.ubb.fmi.orar.feature.form.viewmodel.OnboardingFormViewModel
import com.ubb.fmi.orar.feature.form.viewmodel.model.OnboardingFormUiState
import com.ubb.fmi.orar.feature.form.viewmodel.model.isNextEnabled
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingFormRoute(navController: NavController) {
    val viewModel = koinViewModel<OnboardingFormViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardingFormScreen(
        studyYears = uiState.studyYears,
        selectedStudyYear = uiState.selectedStudyYear,
        selectedSemesterId = uiState.selectedSemesterId,
        selectedUserTypeId = uiState.selectedUserTypeId,
        selectedDegreeId = uiState.selectedDegreeId,
        selectedTeacherTitleId = uiState.selectedTeacherTitleId,
        isNextEnabled = uiState.isNextEnabled,
        onStudyYearClick = viewModel::selectStudyYear,
        onSemesterClick = viewModel::selectSemester,
        onUserTypeClick = viewModel::selectUserType,
        onDegreeClick = viewModel::selectDegree,
        onTeacherTitleClick = viewModel::selectTeacherTitle,
        onNextClick = viewModel::finishOnboarding
    )

    EventHandler(viewModel.events) { event ->
        when(event) {
            OnboardingFormUiState.OnboardingFormEvent.FORM_STUDENT_COMPLETED -> {
                viewModel.unregisterEvent(event)
                navController.navigate(ConfigurationFormNavDestination.StudyLinesForm)
            }

            OnboardingFormUiState.OnboardingFormEvent.FORM_TEACHER_COMPLETED -> {
                viewModel.unregisterEvent(event)
                uiState.selectedTeacherTitleId?.let {
                    navController.navigate(ConfigurationFormNavDestination.TeachersForm(it))
                }
            }
        }
    }
}
