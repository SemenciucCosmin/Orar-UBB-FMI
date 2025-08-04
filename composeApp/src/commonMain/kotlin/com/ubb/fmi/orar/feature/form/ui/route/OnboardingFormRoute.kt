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
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationNavDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingFormRoute(
    navController: NavController,
    title: String
) {
    val viewModel = koinViewModel<OnboardingFormViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardingFormScreen(
        title = title,
        studyYears = uiState.studyYears,
        selectedStudyYear = uiState.selectedStudyYear,
        selectedSemesterId = uiState.selectedSemesterId,
        selectedUserTypeId = uiState.selectedUserTypeId,
        selectedDegreeId = uiState.selectedDegreeId,
        isNextEnabled = uiState.isNextEnabled,
        onStudyYearClick = viewModel::selectStudyYear,
        onSemesterClick = viewModel::selectSemester,
        onUserTypeClick = viewModel::selectUserType,
        onDegreeClick = viewModel::selectDegree,
        onNextClick = viewModel::finishOnboarding
    )

    EventHandler(viewModel.events) { event ->
        when(event) {
            OnboardingFormUiState.OnboardingFormEvent.ONBOARDING_STUDENT_DONE -> {
                viewModel.unregisterEvent(event)
                navController.navigate(ConfigurationNavDestination.StudyLinesForm)
            }

            OnboardingFormUiState.OnboardingFormEvent.ONBOARDING_TEACHER_DONE -> {
                viewModel.unregisterEvent(event)
                navController.navigate(ConfigurationNavDestination.TeachersForm)
            }
        }
    }
}
