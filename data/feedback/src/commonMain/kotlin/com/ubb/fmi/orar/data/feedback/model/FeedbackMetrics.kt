package com.ubb.fmi.orar.data.feedback.model

/**
 * Data class that incorporates all feedback gathered metrics
 */
data class FeedbackMetrics(
    val isFeedbackLoopShown: Boolean,
    val firstUsageTimestamp: Long?,
    val appUsagePoints: Int,
)