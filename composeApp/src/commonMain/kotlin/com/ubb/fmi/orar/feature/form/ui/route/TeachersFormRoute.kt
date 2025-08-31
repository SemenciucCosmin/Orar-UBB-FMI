package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.TeachersFormScreen
import com.ubb.fmi.orar.feature.form.ui.viewmodel.TeachersFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.TeachersFormUiState
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TeachersFormRoute(
    navController: NavController,
    year: Int,
    semesterId: String
) {
    val viewModel = koinViewModel<TeachersFormViewModel>(
        parameters = { parametersOf(year, semesterId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeachersFormScreen(
        uiState = uiState,
        onTeacherClick = viewModel::selectTeacher,
        onSelectFilter = viewModel::selectTeacherTitleFilter,
        onNextClick = viewModel::finishSelection,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp,
    )

    EventHandler(viewModel.events) { event ->
        when(event) {
            TeachersFormUiState.TeachersFormEvent.CONFIGURATION_DONE -> {
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
