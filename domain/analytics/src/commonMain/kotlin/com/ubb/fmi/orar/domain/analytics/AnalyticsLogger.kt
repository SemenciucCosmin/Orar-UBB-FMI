package com.ubb.fmi.orar.domain.analytics

import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent

/**
 * Interface for logging analytics events in Firebase
 */
interface AnalyticsLogger {

    fun logEvent(event: AnalyticsEvent)
}
