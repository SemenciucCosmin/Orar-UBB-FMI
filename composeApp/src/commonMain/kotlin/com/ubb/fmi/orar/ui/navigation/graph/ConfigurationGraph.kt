package com.ubb.fmi.orar.ui.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ubb.fmi.orar.feature.form.ui.route.OnboardingFormRoute
import com.ubb.fmi.orar.feature.form.ui.route.StudyGroupsRoute
import com.ubb.fmi.orar.feature.form.ui.route.StudyLinesRoute
import com.ubb.fmi.orar.feature.form.ui.route.TeachersRoute
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationNavDestination

@Composable
fun ConfigurationGraph(
    navController: NavHostController,
    onboardingFormTitle: String,
) {
    NavHost(
        navController = navController,
        startDestination = ConfigurationNavDestination.OnboardingForm(onboardingFormTitle),
    ) {
        composable<ConfigurationNavDestination.OnboardingForm> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<ConfigurationNavDestination.OnboardingForm>()

            OnboardingFormRoute(
                navController = navController,
                title = args.title
            )
        }

        composable<ConfigurationNavDestination.TeachersForm> { navBackStackEntry ->
            TeachersRoute(navController)
        }

        composable<ConfigurationNavDestination.StudyLinesForm> { navBackStackEntry ->
            StudyLinesRoute(navController)
        }

        composable<ConfigurationNavDestination.StudyGroupsForm> { navBackStackEntry ->
            StudyGroupsRoute(navController)
        }
    }
}