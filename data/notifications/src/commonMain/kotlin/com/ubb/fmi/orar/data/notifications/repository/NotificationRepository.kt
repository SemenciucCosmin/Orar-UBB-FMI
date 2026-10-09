package com.ubb.fmi.orar.data.notifications.repository

import com.ubb.fmi.orar.data.notifications.model.EventNotification

/**
 * Platform-agnostic API for scheduling and cancelling local notifications for timetable events.
 * Android implements it with [android.app.AlarmManager] + a [android.content.BroadcastReceiver];
 * iOS implements it with `UNUserNotificationCenter`.
 */
interface NotificationRepository {

    /**
     * Schedules recurring local notifications for every entry of [notifications], based on its
     * [EventNotification.day], start time and [EventNotification.frequency], and bounded to the
     * teaching weeks of its [EventNotification.academicYear]. Calling this again for the same
     * [EventNotification.id] replaces anything previously scheduled for it.
     *
     * Scheduling is expressed as a batch rather than one event at a time because iOS caps an app
     * at 64 pending local notifications, and the implementation has to divide that budget across
     * the whole batch to avoid the first few events starving the rest.
     */
    suspend fun schedule(notifications: List<EventNotification>)

    /**
     * Cancels any pending/scheduled notification previously scheduled for the event with [id].
     * Safe to call even if nothing is currently scheduled for [id].
     */
    suspend fun cancel(id: String)
}
