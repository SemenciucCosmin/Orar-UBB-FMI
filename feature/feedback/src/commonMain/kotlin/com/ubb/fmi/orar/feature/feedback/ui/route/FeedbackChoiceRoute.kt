package com.ubb.fmi.orar.feature.feedback.ui.route

import androidx.compose.runtime.Composable
import com.ubb.fmi.orar.feature.feedback.ui.screen.FeedbackChoiceScreen
import com.ubb.fmi.orar.feature.feedback.ui.viewmodel.FeedbackViewModel
import com.ubb.fmi.orar.ui.navigation.destination.FeedbackNavDestination
import org.koin.compose.viewmodel.koinViewModel

/**
 * Route for feedback response choice.
 */
@Composable
fun FeedbackChoiceRoute(
    onNavigate: (Any) -> Unit,
    onFinish: () -> Unit
) {
    val viewModel: FeedbackViewModel = koinViewModel()

    FeedbackChoiceScreen(
        onFeedbackChoiceClick = { feedbackChoice ->
            onNavigate(FeedbackNavDestination.Outcome(feedbackChoice.id))
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
