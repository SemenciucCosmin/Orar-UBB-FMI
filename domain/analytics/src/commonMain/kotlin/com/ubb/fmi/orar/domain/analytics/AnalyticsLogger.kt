package com.ubb.fmi.orar.domain.analytics

import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent

/**
 * Interface for logging analytics events in Firebase
 */
interface AnalyticsLogger {

    /**
     * Logs [event] with optional [params]. Keys should come from
     * [com.ubb.fmi.orar.domain.analytics.model.AnalyticsParameter].
     */
    fun logEvent(event: AnalyticsEvent, params: Map<String, Any>? = null)
}
