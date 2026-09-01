package com.ubb.fmi.orar.data.notifications.manager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build

@SuppressLint("MissingPermission")
fun AlarmManager.scheduleExact(type: Int, triggerAtMillis: Long, operation: PendingIntent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !canScheduleExactAlarms()) {
        setAndAllowWhileIdle(type, triggerAtMillis, operation)
    } else {
        setExactAndAllowWhileIdle(type, triggerAtMillis, operation)
    }
}
