package com.ubb.fmi.orar.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.feature.settings.ui.model.AboutLink
import com.ubb.fmi.orar.feature.settings.viewmodel.model.AboutUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for the about screen, reporting which external links get used.
 */
class AboutViewModel(
    private val analyticsLogger: AnalyticsLogger,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutUiState())
    val uiState = _uiState.asStateFlow()

    fun onLinkClick(link: AboutLink) {
        val event = when (link) {
            AboutLink.STORE_REVIEW -> AnalyticsEvent.STORE_REVIEW_CLICK
            AboutLink.REPORT_ISSUE -> AnalyticsEvent.REPORT_ISSUE_CLICK
            AboutLink.LEAVE_FEEDBACK -> AnalyticsEvent.LEAVE_FEEDBACK_CLICK
            AboutLink.CONTACT -> AnalyticsEvent.CONTACT_CLICK
            AboutLink.REPOSITORY -> AnalyticsEvent.REPOSITORY_CLICK
            AboutLink.DATA_SOURCE -> AnalyticsEvent.DATA_SOURCE_CLICK
        }

        analyticsLogger.logEvent(event)
    }
}
