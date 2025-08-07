package com.ubb.fmi.orar.ui.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ubb.fmi.orar.feature.timetable.ui.route.MainTimetableRoute
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination

@Composable
fun TimetableGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = TimetableNavDestination.Main,
    ) {
        composable<TimetableNavDestination.Main> { navBackStackEntry ->
            MainTimetableRoute(navController)
        }
    }
}