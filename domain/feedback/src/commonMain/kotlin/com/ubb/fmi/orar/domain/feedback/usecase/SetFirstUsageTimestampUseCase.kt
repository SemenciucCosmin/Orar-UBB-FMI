package com.ubb.fmi.orar.domain.feedback.usecase

import com.ubb.fmi.orar.data.feedback.preferences.FeedbackPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Use case for setting the first usage timestamp if not set yet for feedback loop
 */
class SetFirstUsageTimestampUseCase(
    private val feedbackPreferences: FeedbackPreferences
) {

    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke() {
        val feedbackMetrics = feedbackPreferences.getFeedbackMetrics().firstOrNull()
        if (feedbackMetrics?.firstUsageTimestamp == null) {
            val timestamp = Clock.System.now().toEpochMilliseconds()
            feedbackPreferences.setFirstUsageTimestamp(timestamp)
        }
    }
}