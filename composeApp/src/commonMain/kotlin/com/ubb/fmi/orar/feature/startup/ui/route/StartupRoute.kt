package com.ubb.fmi.orar.feature.startup.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.model.ConfigurationFormType
import com.ubb.fmi.orar.feature.startup.ui.viewmodel.StartupViewModel
import com.ubb.fmi.orar.feature.startup.ui.viewmodel.model.StartupEvent
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StartupRoute(navController: NavController) {
    val viewModel: StartupViewModel = koinViewModel()

    EventHandler(viewModel.events) { event ->
        when (event) {
            StartupEvent.CONFIGURATION_COMPLETE -> {
                navController.navigate(TimetableNavDestination.UserTimetable) {
                    popUpTo(TimetableNavDestination.Startup) {
                        inclusive = true
                        saveState = true
                    }
                }
            }

            StartupEvent.CONFIGURATION_INCOMPLETE -> {
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
