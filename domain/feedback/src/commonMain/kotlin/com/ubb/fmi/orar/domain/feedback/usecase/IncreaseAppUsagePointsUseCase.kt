package com.ubb.fmi.orar.domain.feedback.usecase

import com.ubb.fmi.orar.data.feedback.preferences.FeedbackPreferences
import kotlinx.coroutines.flow.firstOrNull

/**
 * Use case for increasing app usage points for feedback loop
 */
class IncreaseAppUsagePointsUseCase(
    private val feedbackPreferences: FeedbackPreferences
) {

    suspend operator fun invoke() {
        val appUsagePoints = feedbackPreferences.getFeedbackMetrics().firstOrNull()?.appUsagePoints
        appUsagePoints?.let { feedbackPreferences.setAppUsagePoints(it.inc()) }
    }
}