package com.ubb.fmi.orar.feature.notifications.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.notifications.ui.components.NotificationPermissionDialog
import com.ubb.fmi.orar.feature.notifications.ui.components.NotificationsScreen
import com.ubb.fmi.orar.feature.notifications.ui.viewmodel.NotificationsViewModel
import com.ubb.fmi.orar.feature.notifications.ui.viewmodel.model.NotificationsUiState.NotificationsUiEvent
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable function that represents the notifications settings route in the application.
 * It initializes the NotificationsViewModel and observes its UI state.
 *
 * @param navController The NavController used for navigation within the app.
 */
@Composable
fun NotificationsRoute(navController: NavController) {
    val viewModel: NotificationsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(viewModel) {
        viewModel.refreshNotificationPermission()
        onPauseOrDispose {}
    }

    NotificationsScreen(
        uiState = uiState,
        onBack = navController::navigateUp,
        onNotificationsEnabledChange = viewModel::setNotificationsEnabled,
        onNotificationAdvanceMinutesChange = viewModel::setNotificationAdvanceMinutes,
    )

    EventHandler(viewModel.events) { event ->
        when (event) {
            NotificationsUiEvent.NOTIFICATION_PERMISSION_REQUIRED -> {
                NotificationPermissionDialog(
                    onDismiss = { viewModel.unregisterEvent(event) },
                    onOpenSettingsClick = {
                        viewModel.unregisterEvent(event)
                        viewModel.openNotificationPermissionSettings()
                    },
                )
            }
        }
    }
}
