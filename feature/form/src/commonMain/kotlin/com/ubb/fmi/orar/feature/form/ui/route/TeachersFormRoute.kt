package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.timetable.model.Semester
import com.ubb.fmi.orar.feature.form.ui.components.TeachersFormScreen
import com.ubb.fmi.orar.feature.form.ui.viewmodel.TeachersFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.TeachersFormUiState
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.catalog.model.ConfigurationFormType
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Composable route with teachers that are selectable for timetable configuration
 * @param navController: navigation controller for handling navigation actions
 */
@Composable
fun TeachersFormRoute(navController: NavController) {
    val viewModel = koinViewModel<TeachersFormViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val yearTitle = uiState.year?.let { "$it - ${it.inc()}" }
    val semesterTitle = uiState.semester?.let { stringResource(it.labelRes) }
    val title = when {
        yearTitle != null && semesterTitle != null -> "$yearTitle, $semesterTitle"
        else -> String.BLANK
    }

    TeachersFormScreen(
        title = title,
        uiState = uiState,
        onTeacherClick = viewModel::selectTeacher,
        onSelectFilter = viewModel::selectTeacherTitleFilter,
        onChangeSearchQuery = viewModel::setSearchQuery,
        onNextClick = viewModel::finishSelection,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp,
    )

    EventHandler(viewModel.events) { event ->
        when (event) {
            TeachersFormUiState.TeachersFormUiEvent.CONFIGURATION_DONE -> {
                viewModel.unregisterEvent(event)
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
