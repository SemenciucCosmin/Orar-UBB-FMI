package com.ubb.fmi.orar.data.notifications.repository

import com.ubb.fmi.orar.data.notifications.model.EventNotification

/**
 * Platform-agnostic API for scheduling and cancelling local notifications for timetable events.
 * Android implements it with [android.app.AlarmManager] + a [android.content.BroadcastReceiver];
 * iOS implements it with `UNUserNotificationCenter`.
 */
interface NotificationRepository {

    /**
     * Schedules a recurring local notification for [notification], based on its [EventNotification.day],
     * start time and [EventNotification.frequency] (weekly or alternating weeks). Calling this again
     * for the same [EventNotification.id] replaces any previously scheduled notification for it.
     */
    suspend fun schedule(notification: EventNotification)

    /**
     * Cancels any pending/scheduled notification previously scheduled for the event with [id].
     * Safe to call even if nothing is currently scheduled for [id].
     */
    suspend fun cancel(id: String)
}
