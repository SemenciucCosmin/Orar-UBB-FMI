package com.ubb.fmi.orar.feature.feedback.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ubb.fmi.orar.feature.feedback.ui.model.FeedbackChoice
import com.ubb.fmi.orar.feature.feedback.ui.route.FeedbackChoiceRoute
import com.ubb.fmi.orar.feature.feedback.ui.route.FeedbackOutcomeRoute
import com.ubb.fmi.orar.ui.navigation.destination.FeedbackNavDestination
import com.ubb.fmi.orar.ui.navigation.destination.MainNavDestination

/**
 * Navigation graph for feedback routes.
 */

fun NavGraphBuilder.feedbackGraph(navController: NavController) {
    composable<FeedbackNavDestination.Choice> {
        FeedbackChoiceRoute(
            onNavigate = navController::navigate,
            onFinish = {
                navController.navigate(MainNavDestination.UserMain) {
                    popUpTo(MainNavDestination.UserMain) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable<FeedbackNavDestination.Outcome> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<FeedbackNavDestination.Outcome>()

        FeedbackOutcomeRoute(
            feedbackChoice = FeedbackChoice.getById(args.feedbackChoiceId),
            onFinish = {
                navController.navigate(MainNavDestination.UserMain) {
                    popUpTo(MainNavDestination.UserMain) {
                        inclusive = true
                    }
                }
            }
        )
    }
}
