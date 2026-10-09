package com.ubb.fmi.orar.domain.feedback.usecase

import com.ubb.fmi.orar.data.feedback.preferences.FeedbackPreferences

/**
 * Use case for marking the feedback loop as shown
 */
class SetFeedbackShownUseCase(
    private val feedbackPreferences: FeedbackPreferences
) {

    suspend operator fun invoke() {
        feedbackPreferences.setFeedbackLoopShown()
    }
}