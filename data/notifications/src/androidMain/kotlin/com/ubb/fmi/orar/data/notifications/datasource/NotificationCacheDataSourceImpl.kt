package com.ubb.fmi.orar.data.notifications.datasource

import android.app.AlarmManager
import android.content.Context

/**
 * Default Android [NotificationCacheDataSource], lazily resolving the system [AlarmManager]
 * service from [context].
 */
class NotificationCacheDataSourceImpl(
    private val context: Context
) : NotificationCacheDataSource {

    private val alarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    override fun getNotificationManager(): AlarmManager {
        return alarmManager
    }
}
