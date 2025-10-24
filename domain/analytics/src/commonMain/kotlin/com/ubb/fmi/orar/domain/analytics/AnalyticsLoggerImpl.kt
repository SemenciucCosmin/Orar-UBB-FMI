package com.ubb.fmi.orar.domain.analytics

import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics

/**
 * Class for logging analytics events in Firebase
 */
class AnalyticsLoggerImpl : AnalyticsLogger {

    override fun logEvent(event: AnalyticsEvent) {
        Firebase.analytics.logEvent(event.id, null)
    }
}
