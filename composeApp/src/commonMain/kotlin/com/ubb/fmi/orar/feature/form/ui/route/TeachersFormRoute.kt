package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.TeachersFormScreen
import com.ubb.fmi.orar.feature.form.ui.model.ConfigurationFormType
import com.ubb.fmi.orar.feature.form.ui.model.Semester
import com.ubb.fmi.orar.feature.form.ui.viewmodel.TeachersFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.TeachersFormUiState
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TeachersFormRoute(
    navController: NavController,
    year: Int,
    semesterId: String
) {
    val semester = Semester.getById(semesterId)
    val viewModel = koinViewModel<TeachersFormViewModel>(
        parameters = { parametersOf(year, semesterId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeachersFormScreen(
        title = "$year - ${year.inc()}, ${stringResource(semester.labelRes)}",
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
                    popUpTo(
                        ConfigurationFormNavDestination.OnboardingForm(
                            configurationFormTypeId = ConfigurationFormType.STARTUP.id
                        )
                    ) {
                        inclusive = true
                        saveState = true
                    }
                }
            }
        }
    }
}
