package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.GroupsFormScreen
import com.ubb.fmi.orar.feature.form.ui.viewmodel.GroupsFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.GroupsFromUiState
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.catalog.model.ConfigurationFormType
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Composable route that displays the list of groups for a certain study line that can
 * be selectable for configuring personal timetable
 */
@Composable
fun GroupsFormRoute(navController: NavController) {
    val viewModel = koinViewModel<GroupsFormViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GroupsFormScreen(
        uiState = uiState,
        onGroupClick = viewModel::selectGroup,
        onNextClick = viewModel::finishSelection,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp,
    )

    EventHandler(viewModel.events) { event ->
        when (event) {
            GroupsFromUiState.GroupsFromUiEvent.CONFIGURATION_DONE -> {
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
