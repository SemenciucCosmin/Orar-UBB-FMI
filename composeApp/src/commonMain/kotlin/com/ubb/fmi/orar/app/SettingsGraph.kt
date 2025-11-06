package com.ubb.fmi.orar.app

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ubb.fmi.orar.feature.settings.ui.route.SettingsRoute
import com.ubb.fmi.orar.feature.settings.ui.route.ThemeRoute
import com.ubb.fmi.orar.ui.navigation.destination.SettingsNavDestination

/**
 * Navigation graph for the settings feature in the Orar UBB FMI application.
 * This function defines the routes for various settings-related screens such as theme,
 * and configuration.
 * Each screen is represented by a composable function that handles the specific route logic.
 */
fun NavGraphBuilder.settingsGraph(navController: NavController) {
    composable<SettingsNavDestination.Settings> { navBackStackEntry ->
        SettingsRoute(navController)
    }

    composable<SettingsNavDestination.Theme> { navBackStackEntry ->
        ThemeRoute(navController)
    }
}