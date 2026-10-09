package com.ubb.fmi.orar.domain.feedback.usecase

import com.ubb.fmi.orar.data.feedback.preferences.FeedbackPreferences
import kotlin.time.Clock

/**
 * Use case postponing feedback loop
 */
class PostponeFeedbackLoopUseCase(
    private val feedbackPreferences: FeedbackPreferences
) {

    suspend operator fun invoke() {
        val currentMillis = Clock.System.now().toEpochMilliseconds()
        val postponedTimeMillis = currentMillis + POSTPONE_TIME_MILLIS
        feedbackPreferences.setPostponedTimestamp(postponedTimeMillis)
    }

    companion object {
        private const val POSTPONE_TIME_MILLIS = 1000L * 60 * 60 * 24 // 1 day
    }
}