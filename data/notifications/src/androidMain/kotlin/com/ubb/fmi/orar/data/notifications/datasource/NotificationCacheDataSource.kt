package com.ubb.fmi.orar.data.notifications.datasource

import android.app.AlarmManager

interface NotificationCacheDataSource {

    fun getNotificationManager(): AlarmManager
}
