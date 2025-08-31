package com.ubb.fmi.orar.feature.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination

@Composable
fun AppGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = TimetableNavDestination.Startup,
    ) {
        configurationFormGraph(navController)
        timetableGraph(navController)
    }
}