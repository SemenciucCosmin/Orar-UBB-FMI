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
                title = args.title,
                navController = navController
            )
        }

        composable<ConfigurationNavDestination.TeachersForm> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<ConfigurationNavDestination.TeachersForm>()
            TeachersFormRoute(
                teacherTitleId = args.teacherTitleId,
                navController = navController
            )
        }

        composable<ConfigurationNavDestination.StudyLinesForm> { navBackStackEntry ->
            StudyLinesRoute(navController)
        }

        composable<ConfigurationNavDestination.StudyGroupsForm> { navBackStackEntry ->
            StudyGroupsRoute(navController)
        }
    }
}