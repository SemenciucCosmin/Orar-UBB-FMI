package com.ubb.fmi.orar.feature.feedback.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ubb.fmi.orar.feature.feedback.ui.route.FeedbackChoiceRoute
import com.ubb.fmi.orar.feature.feedback.ui.route.FeedbackOutcomeRoute

/**
 * Navigation graph for feedback routes.
 */
@Composable
fun FeedbackNavigationGraph(
    navController: NavHostController,
    onFinish: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = FeedbackNavDestination.Choice,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        composable<FeedbackNavDestination.Choice> {
            FeedbackChoiceRoute(
                navController = navController,
                onFinish = onFinish
            )
        }

        composable<FeedbackNavDestination.Outcome> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<FeedbackNavDestination.Outcome>()

            FeedbackOutcomeRoute(
                feedbackChoice = args.feedbackChoice,
                onFinish = onFinish
            )
        }
    }
}
