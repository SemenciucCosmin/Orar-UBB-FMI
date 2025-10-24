package com.ubb.fmi.orar.domain.feedback.usecase

import com.ubb.fmi.orar.data.feedback.preferences.FeedbackPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Use case for determining if feedback loop is ready to show
 */
class GetFeedbackLoopReadinessUseCase(
    private val feedbackPreferences: FeedbackPreferences,
) {

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
    suspend operator fun invoke(): Flow<Boolean> {
        return feedbackPreferences.getFeedbackMetrics().mapLatest { feedbackMetrics ->
            val currentMillis = Clock.System.now().toEpochMilliseconds()
            val firstUsageTimestamp = feedbackMetrics.firstUsageTimestamp ?: DEFAULT_MILLIS
            val passedMillis = currentMillis - firstUsageTimestamp

            val isPostponeExpired = feedbackMetrics.postponedTimestamp <= currentMillis
            val isEnoughAppUsageDays = passedMillis >= MIN_APP_USAGE_DAYS_MILLIS
            val isEnoughAppUsagePoints = feedbackMetrics.appUsagePoints >= MIN_APP_USAGE_POINTS

            listOf(
                isPostponeExpired,
                isEnoughAppUsageDays,
                isEnoughAppUsagePoints,
                feedbackMetrics.isFeedbackLoopShown
            ).all { it }
        }
    }

    companion object {
        private const val DEFAULT_MILLIS = 0L
        private const val MIN_APP_USAGE_POINTS = 15
        private const val MIN_APP_USAGE_DAYS_MILLIS = 1000L * 60 * 60 * 24 * 5 // 5 days
    }
}