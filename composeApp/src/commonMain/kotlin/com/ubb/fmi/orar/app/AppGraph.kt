package com.ubb.fmi.orar.app

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.ui.navigation.destination.MainNavDestination

/**
 * Main navigation graph for the Orar UBB FMI application.
 * This function sets up the navigation host and defines the start destination.
 * It includes the configuration form and timetable graphs.
 */
@Composable
fun AppGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainNavDestination.Startup,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        configurationFormGraph(navController)
        mainGraph(navController)
        exploreGraph(navController)
        settingsGraph(navController)
    }
}