package com.ubb.fmi.orar.app

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ubb.fmi.orar.feature.form.ui.route.GroupsFormRoute
import com.ubb.fmi.orar.feature.form.ui.route.OnboardingFormRoute
import com.ubb.fmi.orar.feature.form.ui.route.StudyLinesFormRoute
import com.ubb.fmi.orar.feature.form.ui.route.TeachersFormRoute
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination

/**
 * Navigation graph for configuration forms in the Orar UBB FMI application.
 * This function defines the routes for various configuration forms such as onboarding,
 * teachers, study lines, and groups.
 * Each form is represented by a composable function that handles the specific form logic.
 */
fun NavGraphBuilder.configurationFormGraph(navController: NavController) {
    composable<ConfigurationFormNavDestination.OnboardingForm> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<ConfigurationFormNavDestination.OnboardingForm>()
        OnboardingFormRoute(
            navController = navController,
            configurationFormTypeId = args.configurationFormTypeId
        )
    }

    composable<ConfigurationFormNavDestination.TeachersForm> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<ConfigurationFormNavDestination.TeachersForm>()
        TeachersFormRoute(
            navController = navController,
            year = args.year,
            semesterId = args.semesterId
        )
    }

    composable<ConfigurationFormNavDestination.StudyLinesForm> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<ConfigurationFormNavDestination.StudyLinesForm>()
        StudyLinesFormRoute(
            navController = navController,
            year = args.year,
            semesterId = args.semesterId
        )
    }

    composable<ConfigurationFormNavDestination.GroupsForm> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<ConfigurationFormNavDestination.GroupsForm>()
        GroupsFormRoute(
            navController = navController,
            year = args.year,
            semesterId = args.semesterId,
            fieldId = args.fieldId,
            studyLevelId = args.studyLevelId,
            studyLineDegreeId = args.studyLineDegreeId,
        )
    }
}
