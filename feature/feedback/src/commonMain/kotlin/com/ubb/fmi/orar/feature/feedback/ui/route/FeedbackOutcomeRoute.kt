package com.ubb.fmi.orar.feature.feedback.ui.route

import androidx.compose.runtime.Composable
import com.ubb.fmi.orar.domain.extensions.openUrl
import com.ubb.fmi.orar.feature.feedback.ui.model.FeedbackChoice
import com.ubb.fmi.orar.feature.feedback.ui.screen.FeedbackOutcomeScreen
import com.ubb.fmi.orar.feature.feedback.ui.viewmodel.FeedbackViewModel
import com.ubb.fmi.orar.ui.catalog.extensions.getContext
import com.ubb.fmi.orar.ui.catalog.extensions.getStoreAppUrl
import com.ubb.fmi.orar.ui.catalog.extensions.showToast
import com.ubb.fmi.orar.ui.catalog.model.ToastLength
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_generic_error_message
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val FEEDBACK_FORM_URL = "https://forms.gle/T1ZK6aB3NCVphqfZ8"

/**
 * Route for feedback response handling.
 */
@Composable
fun FeedbackOutcomeRoute(
    feedbackChoice: FeedbackChoice,
    onFinish: () -> Unit,
) {
    val viewModel: FeedbackViewModel = koinViewModel()
    val context = getContext()
    val toastMessage = stringResource(Res.string.lbl_generic_error_message)

    FeedbackOutcomeScreen(
        feedbackChoice = feedbackChoice,
        onAskMeLaterClick = {
            onFinish()
            viewModel.postponeFeedback()
        },
        onDismissClick = {
            onFinish()
            viewModel.dismissFeedback()
        },
        onSecondaryActionClick = {
            openUrl(FEEDBACK_FORM_URL, context) {
                showToast(context, toastMessage, ToastLength.SHORT)
            }

            onFinish()
            viewModel.markFeedbackShown()
        },
        onActionClick = {
            when (feedbackChoice) {
                FeedbackChoice.OK,
                FeedbackChoice.POOR -> {
                    openUrl(FEEDBACK_FORM_URL, context) {
                        showToast(context, toastMessage, ToastLength.SHORT)
                    }

                    onFinish()
                    viewModel.markFeedbackShown()
                }

                FeedbackChoice.GREAT -> {
                    openUrl(getStoreAppUrl(), context) {
                        showToast(context, toastMessage, ToastLength.SHORT)
                    }

                    onFinish()
                    viewModel.markFeedbackShown()
                }
            }
        }
    )
}
