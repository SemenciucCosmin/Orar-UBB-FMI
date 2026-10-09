package com.ubb.fmi.orar.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent

/**
 * ViewModel for the settings screen, reporting which external support links get used.
 */
class SettingsViewModel(
    private val analyticsLogger: AnalyticsLogger,
) : ViewModel() {

    fun onRepositoryClick() {
        analyticsLogger.logEvent(AnalyticsEvent.REPOSITORY_CLICK)
    }

    fun onReportIssueClick() {
        analyticsLogger.logEvent(AnalyticsEvent.REPORT_ISSUE_CLICK)
    }

    fun onLeaveFeedbackClick() {
        analyticsLogger.logEvent(AnalyticsEvent.LEAVE_FEEDBACK_CLICK)
    }
}
