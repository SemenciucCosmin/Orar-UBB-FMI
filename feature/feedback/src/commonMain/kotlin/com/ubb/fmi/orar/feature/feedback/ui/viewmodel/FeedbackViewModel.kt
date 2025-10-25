package com.ubb.fmi.orar.feature.feedback.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.domain.feedback.usecase.PostponeFeedbackLoopUseCase
import com.ubb.fmi.orar.domain.feedback.usecase.SetFeedbackShownUseCase
import kotlinx.coroutines.launch

/**
 * ViewModel class handling the state for the feedback and providing support to action methods.
 */
class FeedbackViewModel(
    private val setFeedbackShownUseCase: SetFeedbackShownUseCase,
    private val postponeFeedbackLoopUseCase: PostponeFeedbackLoopUseCase
) : ViewModel() {

    fun markFeedbackShown() {
        viewModelScope.launch { setFeedbackShownUseCase() }
    }

    fun postponeFeedback() {
        viewModelScope.launch { postponeFeedbackLoopUseCase() }
    }

    companion object {
        const val SUPPORT_URL = "https://support.garmin.com"
        const val RATE_APP_URL = "market://details?id=com.garmin.connectiq"
        const val RATE_APP_WEB_URL = "https://play.google.com/store/apps/details?id=com.garmin.connectiq"
    }
}
