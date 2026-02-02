package com.ubb.fmi.orar.feature.feedback.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.domain.feedback.usecase.PostponeFeedbackLoopUseCase
import com.ubb.fmi.orar.domain.feedback.usecase.SetFeedbackShownUseCase
import com.ubb.fmi.orar.feature.feedback.ui.model.FeedbackChoice
import kotlinx.coroutines.launch

/**
 * ViewModel class handling the state for the feedback and providing support to action methods.
 */
class FeedbackViewModel(
    private val analyticsLogger: AnalyticsLogger,
    private val setFeedbackShownUseCase: SetFeedbackShownUseCase,
    private val postponeFeedbackLoopUseCase: PostponeFeedbackLoopUseCase,
) : ViewModel() {

    init {
        analyticsLogger.logEvent(AnalyticsEvent.FEEDBACK_SHOW)
    }

    fun markFeedbackShown() {
        viewModelScope.launch { setFeedbackShownUseCase() }
    }

    fun postponeFeedback() {
        analyticsLogger.logEvent(AnalyticsEvent.FEEDBACK_POSTPONE)
        viewModelScope.launch { postponeFeedbackLoopUseCase() }
    }

    fun dismissFeedback() {
        analyticsLogger.logEvent(AnalyticsEvent.FEEDBACK_DISMISS)
        viewModelScope.launch { setFeedbackShownUseCase() }
    }

    fun onFeedbackChoiceSelected(feedbackChoice: FeedbackChoice) {
        when (feedbackChoice) {
            FeedbackChoice.POOR -> analyticsLogger.logEvent(AnalyticsEvent.FEEDBACK_NEGATIVE)
            FeedbackChoice.OK -> analyticsLogger.logEvent(AnalyticsEvent.FEEDBACK_NEUTRAL)
            FeedbackChoice.GREAT -> analyticsLogger.logEvent(AnalyticsEvent.FEEDBACK_POSITIVE)
        }
    }
}
