package com.ubb.fmi.orar.feature.settings.ui.route

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.model.ConfigurationFormType
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.SettingsNavDestination
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.feature.settings.generated.resources.Res
import orar_ubb_fmi.feature.settings.generated.resources.lbl_change_configuration
import orar_ubb_fmi.feature.settings.generated.resources.lbl_settings
import orar_ubb_fmi.feature.settings.generated.resources.lbl_theme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Route for all settings options.
 * Theme and configuration change
 */
@Composable
fun SettingsRoute(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_settings),
                onBack = navController::navigateUp
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            modifier = Modifier
                .padding(paddingValues)
                .padding(Pds.spacing.Medium)
        ) {
            ListItemClickable(
                headline = stringResource(Res.string.lbl_change_configuration),
                onClick = {
                    navController.navigate(
                        ConfigurationFormNavDestination.OnboardingForm(
                            configurationFormTypeId = ConfigurationFormType.SETTINGS.id
                        )
                    )
                }
            )

            ListItemClickable(
                headline = stringResource(Res.string.lbl_theme),
                onClick = { navController.navigate(SettingsNavDestination.Theme) }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsRoute() {
    OrarUbbFmiTheme {
        SettingsRoute(
            navController = rememberNavController()
        )
    }
}
