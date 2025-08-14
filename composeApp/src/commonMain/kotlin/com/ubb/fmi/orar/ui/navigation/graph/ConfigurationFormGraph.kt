package com.ubb.fmi.orar.ui.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ubb.fmi.orar.feature.form.ui.route.OnboardingFormRoute
import com.ubb.fmi.orar.feature.form.ui.route.StudyGroupsRoute
import com.ubb.fmi.orar.feature.form.ui.route.StudyLinesRoute
import com.ubb.fmi.orar.feature.form.ui.route.TeachersFormRoute
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination

@Composable
fun ConfigurationFormGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ConfigurationFormNavDestination.OnboardingForm,
    ) {
        composable<ConfigurationFormNavDestination.OnboardingForm> { navBackStackEntry ->
            OnboardingFormRoute(navController)
        }

        composable<ConfigurationFormNavDestination.TeachersForm> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<ConfigurationFormNavDestination.TeachersForm>()
            TeachersFormRoute(teacherTitleId = args.teacherTitleId)
        }

        composable<ConfigurationFormNavDestination.StudyLinesForm> { navBackStackEntry ->
            StudyLinesRoute(navController)
        }

        composable<ConfigurationFormNavDestination.StudyGroupsForm> { navBackStackEntry ->
            StudyGroupsRoute()
        }
    }
}