package com.ubb.fmi.orar.feature.settings.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ubb.fmi.orar.domain.extensions.getAppVersion
import com.ubb.fmi.orar.domain.extensions.openUrl
import com.ubb.fmi.orar.feature.settings.ui.components.SettingsScreen
import com.ubb.fmi.orar.feature.settings.viewmodel.SettingsViewModel
import com.ubb.fmi.orar.feature.settings.viewmodel.model.SettingsUiEvent
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.catalog.extensions.getContext
import com.ubb.fmi.orar.ui.catalog.extensions.showToast
import com.ubb.fmi.orar.ui.catalog.model.ConfigurationFormType
import com.ubb.fmi.orar.ui.catalog.model.ToastLength
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.SettingsNavDestination
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_refresh_data_success_message
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val GITHUB_URL = "https://github.com/SemenciucCosmin/Orar-UBB-FMI"
private const val DEVELOPER_NAME = "Semenciuc Cosmin"

/**
 * Route for all settings options.
 * Theme and configuration change
 */
@Composable
fun SettingsRoute(navController: NavController) {
    val viewModel: SettingsViewModel = koinViewModel()
    val context = getContext()
    val appVersion = getAppVersion(context)

    SettingsScreen(
        appVersion = appVersion,
        developerName = DEVELOPER_NAME,
        onGithubUrlClick = { openUrl(GITHUB_URL, context) {} },
        onBack = navController::navigateUp,
        onThemeClick = { navController.navigate(SettingsNavDestination.Theme) },
        onAddPersonalEventClick = {
            navController.navigate(SettingsNavDestination.AddPersonalEvent)
        },
        onChangeConfigurationClick = {
            navController.navigate(
                ConfigurationFormNavDestination.OnboardingForm(
                    configurationFormTypeId = ConfigurationFormType.SETTINGS.id
                )
            )
        }
    )

    EventHandler(viewModel.events) { event ->
        when (event) {
            SettingsUiEvent.SUCCESSFUL_REFRESH -> {
                viewModel.unregisterEvent(event)
                showToast(
                    context = context,
                    message = stringResource(Res.string.lbl_refresh_data_success_message),
                    length = ToastLength.SHORT
                )
            }
        }
    }
}
