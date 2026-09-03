package com.ubb.fmi.orar.data.notifications.datasource

import android.app.AlarmManager

/**
 * Provides access to the platform's [AlarmManager], used to schedule/cancel the alarms
 * that back local event notifications on Android.
 */
interface NotificationCacheDataSource {

    /**
     * Returns the app's shared [AlarmManager] instance.
     */
    fun getNotificationManager(): AlarmManager
}
