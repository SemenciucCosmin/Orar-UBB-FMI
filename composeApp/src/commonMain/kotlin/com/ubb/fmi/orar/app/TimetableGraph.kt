package com.ubb.fmi.orar.app

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ubb.fmi.orar.feature.explore.ui.route.ExploreRoute
import com.ubb.fmi.orar.feature.news.ui.route.NewsRoute
import com.ubb.fmi.orar.feature.startup.ui.route.StartupRoute
import com.ubb.fmi.orar.feature.usertimetable.ui.route.UserTimetableRoute
import com.ubb.fmi.orar.ui.navigation.destination.MainNavDestination

/**
 * Navigation graph for the timetable feature in the Orar UBB FMI application.
 * This function defines the routes for various timetable-related screens such as user timetable,
 * study lines, groups, teachers, subjects, and rooms.
 * Each screen is represented by a composable function that handles the specific route logic.
 */
fun NavGraphBuilder.mainGraph(navController: NavController) {
    composable<MainNavDestination.Startup> { navBackStackEntry ->
        StartupRoute(navController)
    }

    composable<MainNavDestination.UserMain> { navBackStackEntry ->
        UserTimetableRoute(navController)
    }

    composable<MainNavDestination.News> { navBackStackEntry ->
        NewsRoute(navController)
    }

    composable<MainNavDestination.Explore> { navBackStackEntry ->
        ExploreRoute(navController)
    }
}