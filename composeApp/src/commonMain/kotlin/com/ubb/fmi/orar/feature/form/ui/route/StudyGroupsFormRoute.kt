package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.StudyGroupsFormScreen
import com.ubb.fmi.orar.feature.form.ui.viewmodel.StudyGroupsFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyGroupsFromUiState
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun StudyGroupsFormRoute(
    navController: NavController,
    year: Int,
    semesterId: String,
    studyLineBaseId: String,
    studyLineYearId: String,
    studyLineDegreeId: String,
) {
    val viewModel = koinViewModel<StudyGroupsFormViewModel>(
        parameters = {
            parametersOf(year, semesterId, studyLineBaseId, studyLineYearId, studyLineDegreeId)
        }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StudyGroupsFormScreen(
        uiState = uiState,
        onStudyGroupClick = viewModel::selectStudyGroup,
        onNextClick = viewModel::finishSelection,
        onRetryClick = viewModel::retry
    )

    EventHandler(viewModel.events) { event ->
        when(event) {
            StudyGroupsFromUiState.StudyGroupsFromEvent.CONFIGURATION_DONE -> {
                navController.navigate(TimetableNavDestination.UserTimetable) {
                    popUpTo(ConfigurationFormNavDestination.OnboardingForm) {
                        inclusive = true
                        saveState = true
                    }
                }
            }
        }
    }
}
