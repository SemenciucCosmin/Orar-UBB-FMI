package com.ubb.fmi.orar.feature.notifications.ui.viewmodel.model

/**
 * Represents the UI state for the notifications settings screen.
 *
 * @property notificationsEnabled Whether event notifications are enabled overall.
 * @property notificationAdvanceMinutes How many minutes in advance of an event's start its
 * notification should fire.
 */
data class NotificationsUiState(
    val notificationsEnabled: Boolean = DEFAULT_NOTIFICATIONS_ENABLED,
    val notificationAdvanceMinutes: Int = DEFAULT_NOTIFICATION_ADVANCE_MINUTES,
    val notificationAdvanceHours: Int = DEFAULT_NOTIFICATION_ADVANCE_HOURS,
) {
    companion object {
        private const val DEFAULT_NOTIFICATIONS_ENABLED = true
        private const val DEFAULT_NOTIFICATION_ADVANCE_MINUTES = 30
        private const val DEFAULT_NOTIFICATION_ADVANCE_HOURS = 0
    }
}
