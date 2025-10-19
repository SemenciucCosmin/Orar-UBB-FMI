package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.OnboardingFormScreen
import com.ubb.fmi.orar.feature.form.ui.viewmodel.OnboardingFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.OnboardingFormUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.isNextEnabled
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.catalog.model.ConfigurationFormType
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable route with multiple available choices for timetable configuration.
 * @param navController: navigation controller for handling navigation actions
 * @param configurationFormTypeId: id of the configuration form type, either STARTUP or SETTINGS
 */
@Composable
fun OnboardingFormRoute(
    navController: NavController,
    configurationFormTypeId: String,
) {
    val viewModel = koinViewModel<OnboardingFormViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val configurationFormType = ConfigurationFormType.getById(configurationFormTypeId)

    OnboardingFormScreen(
        configurationFormType = configurationFormType,
        studyYears = uiState.studyYears,
        selectedStudyYear = uiState.selectedStudyYear,
        selectedSemesterId = uiState.selectedSemesterId,
        selectedUserTypeId = uiState.selectedUserTypeId,
        isNextEnabled = uiState.isNextEnabled,
        onStudyYearClick = viewModel::selectStudyYear,
        onSemesterClick = viewModel::selectSemester,
        onUserTypeClick = viewModel::selectUserType,
        onBack = navController::navigateUp,
        onNextClick = viewModel::finishSelection
    )

    EventHandler(viewModel.events) { event ->
        when (event) {
            OnboardingFormUiState.OnboardingFormUiEvent.STUDENT_FINISH -> {
                viewModel.unregisterEvent(event)
                navController.navigate(ConfigurationFormNavDestination.StudyLinesForm)
            }

            OnboardingFormUiState.OnboardingFormUiEvent.TEACHER_FINISH -> {
                viewModel.unregisterEvent(event)
                navController.navigate(ConfigurationFormNavDestination.TeachersForm)
            }
        }
    }
}
