package com.ubb.fmi.orar.data.notifications.manager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build

/**
 * Schedules [operation] to fire at [triggerAtMillis], using the most precise API available.
 *
 * Falls back to the inexact [AlarmManager.setAndAllowWhileIdle] on Android 12+ (API 31+) when
 * the user hasn't granted the "Alarms & reminders" exact-alarm permission, since
 * [AlarmManager.setExactAndAllowWhileIdle] would otherwise throw a [SecurityException]. The
 * inexact fallback may be delayed by Doze/App Standby batching, so the notification can fire
 * later than [triggerAtMillis].
 */
@SuppressLint("MissingPermission")
fun AlarmManager.scheduleExact(type: Int, triggerAtMillis: Long, operation: PendingIntent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !canScheduleExactAlarms()) {
        setAndAllowWhileIdle(type, triggerAtMillis, operation)
    } else {
        setExactAndAllowWhileIdle(type, triggerAtMillis, operation)
    }
}
