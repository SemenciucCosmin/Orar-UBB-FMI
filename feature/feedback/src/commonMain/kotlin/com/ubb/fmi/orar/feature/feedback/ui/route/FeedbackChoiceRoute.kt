package com.ubb.fmi.orar.feature.feedback.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.feedback.ui.navigation.FeedbackNavDestination
import com.ubb.fmi.orar.feature.feedback.ui.screen.FeedbackChoiceScreen
import com.ubb.fmi.orar.feature.feedback.ui.viewmodel.FeedbackViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Route for feedback response choice.
 */
@Composable
fun FeedbackChoiceRoute(
    navController: NavController,
    onFinish: () -> Unit,
) {
    val viewModel: FeedbackViewModel = koinViewModel()

    FeedbackChoiceScreen(
        onFeedbackChoiceClick = { feedbackChoice ->
            val destination = FeedbackNavDestination.Outcome(feedbackChoice)
            navController.navigate(destination)
        },
        onAskMeLaterClick = {
            onFinish()
            viewModel.postponeFeedback()
        },
        onDontAskAgainClick = {
            onFinish()
            viewModel.markFeedbackShown()
        }
    )
}
