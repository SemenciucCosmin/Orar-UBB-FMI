package com.ubb.fmi.orar.feature.dialogs.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.dialogs.ui.components.AppUpdateAnnouncement
import com.ubb.fmi.orar.feature.dialogs.ui.viewmodel.DialogsViewModel
import com.ubb.fmi.orar.feature.dialogs.ui.viewmodel.model.DialogsUiEvent
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.navigation.destination.FeedbackNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.MainNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.SettingsNavDestination
import org.koin.compose.viewmodel.koinViewModel

/**
 * Route for managing dialog events
 */
@Composable
fun DialogsRoute(navController: NavController) {
    val viewModel: DialogsViewModel = koinViewModel()

    EventHandler(viewModel.events) { event ->
        when (event) {
            DialogsUiEvent.UPDATE_ANNOUNCEMENT -> {
                AppUpdateAnnouncement(
                    onDismiss = {
                        viewModel.unregisterEvent(event)
                        viewModel.setUpdateAnnouncementShown()
                    },
                    onSupportClick = {
                        viewModel.unregisterEvent(event)
                        viewModel.setUpdateAnnouncementShown()
                        navController.navigate(SettingsNavDestination.About)
                    }
                )
            }

            DialogsUiEvent.FEEDBACK_LOOP -> {
                navController.navigate(FeedbackNavDestination.Choice) {
                    popUpTo(MainNavDestination.UserMain()) {
                        inclusive = true
                    }
                }

                viewModel.unregisterEvent(event)
            }
        }
    }
}