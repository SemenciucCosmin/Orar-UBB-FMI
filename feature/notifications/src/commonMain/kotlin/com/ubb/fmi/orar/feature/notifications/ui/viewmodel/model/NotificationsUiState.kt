package com.ubb.fmi.orar.feature.notifications.ui.viewmodel.model

import com.ubb.fmi.orar.ui.catalog.viewmodel.model.UiEvent

/**
 * Represents the UI state for the notifications settings screen.
 *
 * @property notificationsEnabled Whether event notifications are enabled overall.
 * @property notificationAdvanceMinutes How many minutes in advance of an event's start its
 * notification should fire.
 * @property isNotificationPermissionGranted Whether the system allows the app to post
 * notifications.
 */
data class NotificationsUiState(
    val notificationsEnabled: Boolean = DEFAULT_NOTIFICATIONS_ENABLED,
    val notificationAdvanceMinutes: Int = DEFAULT_NOTIFICATION_ADVANCE_MINUTES,
    val notificationAdvanceHours: Int = DEFAULT_NOTIFICATION_ADVANCE_HOURS,
    val isNotificationPermissionGranted: Boolean = true,
) {
    /**
     * Whether notifications are effectively on: enabled by the user and allowed by the system.
     */
    val areNotificationsOn: Boolean
        get() = notificationsEnabled && isNotificationPermissionGranted

    /**
     * One-off events of the notifications settings screen.
     */
    enum class NotificationsUiEvent : UiEvent {
        NOTIFICATION_PERMISSION_REQUIRED
    }

    companion object {
        private const val DEFAULT_NOTIFICATIONS_ENABLED = true
        private const val DEFAULT_NOTIFICATION_ADVANCE_MINUTES = 30
        private const val DEFAULT_NOTIFICATION_ADVANCE_HOURS = 0
    }
}
