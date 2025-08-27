package com.ubb.fmi.orar.feature.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination

@Composable
fun AppGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = TimetableNavDestination.Startup,
    ) {
        configurationFormGraph(navController)
        timetableGraph(navController)
    }
}