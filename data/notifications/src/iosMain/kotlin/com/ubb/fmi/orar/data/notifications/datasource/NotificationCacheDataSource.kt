package com.ubb.fmi.orar.data.notifications.datasource

import platform.UserNotifications.UNUserNotificationCenter

/**
 * Provides access to the shared `UNUserNotificationCenter`, used to schedule/cancel local
 * event notifications on iOS.
 */
interface NotificationCacheDataSource {

    /**
     * Returns the app's current `UNUserNotificationCenter` instance.
     */
    fun getNotificationCenter(): UNUserNotificationCenter
}
