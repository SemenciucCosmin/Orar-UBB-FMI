package com.ubb.fmi.orar.data.feedback.preferences

import com.ubb.fmi.orar.data.feedback.model.FeedbackMetrics
import kotlinx.coroutines.flow.Flow

/**
 * Preferences for feedback feature
 */
interface FeedbackPreferences {

    /**
     * Gets all feedback metrics
     */
    suspend fun getFeedbackMetrics(): Flow<FeedbackMetrics>

    /**
     * Marks feedback loop as shown
     */
    suspend fun setFeedbackLoopShown()

    /**
     * Sets first app usage timestamp
     */
    suspend fun setFirstUsageTimestamp(value: Long)

    /**
     * Sets postponed feed back loop timestamp
     */
    suspend fun setPostponedTimestamp(value: Long)

    /**
     * Sets points of app usage
     */
    suspend fun setAppUsagePoints(value: Int)

    companion object {
        const val PREFERENCES_NAME = "FEEDBACK_PREFERENCES"
    }
}
