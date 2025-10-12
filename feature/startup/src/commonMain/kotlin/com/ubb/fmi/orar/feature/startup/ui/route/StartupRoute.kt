package com.ubb.fmi.orar.feature.startup.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.startup.ui.viewmodel.StartupViewModel
import com.ubb.fmi.orar.feature.startup.ui.viewmodel.model.StartupUiEvent
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.catalog.model.ConfigurationFormType
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable function that defines the startup route of the application.
 *
 * This function listens for events from the StartupViewModel and navigates to the appropriate screen
 * based on whether the configuration is complete or incomplete.
 *
 * @param navController The NavController used for navigation between screens.
 */
@Composable
fun StartupRoute(navController: NavController) {
    val viewModel: StartupViewModel = koinViewModel()

    EventHandler(viewModel.events) { event ->
        when (event) {
            StartupUiEvent.CONFIGURATION_COMPLETE -> {
                navController.navigate(TimetableNavDestination.UserTimetable) {
                    popUpTo(TimetableNavDestination.Startup) {
                        inclusive = true
                        saveState = true
                    }
                }
            }

            StartupUiEvent.CONFIGURATION_INCOMPLETE -> {
                navController.navigate(
                    ConfigurationFormNavDestination.OnboardingForm(
                        configurationFormTypeId = ConfigurationFormType.STARTUP.id
                    )
                ) {
                    popUpTo(TimetableNavDestination.Startup) {
                        inclusive = true
                        saveState = true
                    }
                }
            }
        }
    }
}
