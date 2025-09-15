package com.ubb.fmi.orar.feature.settings.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ubb.fmi.orar.domain.extensions.getAppVersion
import com.ubb.fmi.orar.domain.extensions.openUrl
import com.ubb.fmi.orar.feature.settings.ui.components.SettingsScreen
import com.ubb.fmi.orar.ui.catalog.extensions.getContext
import com.ubb.fmi.orar.ui.catalog.model.ConfigurationFormType
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.SettingsNavDestination

private const val GITHUB_URL = "https://github.com/SemenciucCosmin/Orar-UBB-FMI"
private const val DEVELOPER_NAME = "Semenciuc Cosmin"

/**
 * Route for all settings options.
 * Theme and configuration change
 */
@Composable
fun SettingsRoute(navController: NavController) {
    val context = getContext()
    val appVersion = getAppVersion(context)

    SettingsScreen(
        appVersion = appVersion,
        developerName = DEVELOPER_NAME,
        onGithubUrlClick = { openUrl(GITHUB_URL, context) {} },
        onBack = navController::navigateUp,
        onThemeClick = { navController.navigate(SettingsNavDestination.Theme) },
        onChangeConfigurationClick = {
            navController.navigate(
                ConfigurationFormNavDestination.OnboardingForm(
                    configurationFormTypeId = ConfigurationFormType.SETTINGS.id
                )
            )
        }
    )
}
