package com.ubb.fmi.orar.feature.settings.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ubb.fmi.orar.domain.extensions.getAppVersion
import com.ubb.fmi.orar.feature.settings.ui.components.SettingsScreen
import com.ubb.fmi.orar.feature.settings.viewmodel.SettingsViewModel
import com.ubb.fmi.orar.ui.catalog.extensions.getContext
import com.ubb.fmi.orar.ui.catalog.model.ConfigurationFormType
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.SettingsNavDestination
import org.koin.compose.viewmodel.koinViewModel

/**
 * Route for all settings options.
 * Theme and configuration change
 */
@Composable
fun SettingsRoute(navController: NavController) {
    val context = getContext()
    val appVersion = getAppVersion(context)
    val viewModel: SettingsViewModel = koinViewModel()

    SettingsScreen(
        appVersion = appVersion,
        onBack = navController::navigateUp,
        onThemeClick = { navController.navigate(SettingsNavDestination.Theme) },
        onNotificationsClick = { navController.navigate(SettingsNavDestination.Notifications) },
        onRepositoryClick = viewModel::onRepositoryClick,
        onReportIssueClick = viewModel::onReportIssueClick,
        onLeaveFeedbackClick = viewModel::onLeaveFeedbackClick,
        onChangeConfigurationClick = {
            navController.navigate(
                ConfigurationFormNavDestination.OnboardingForm(
                    configurationFormTypeId = ConfigurationFormType.SETTINGS.id
                )
            )
        }
    )
}
