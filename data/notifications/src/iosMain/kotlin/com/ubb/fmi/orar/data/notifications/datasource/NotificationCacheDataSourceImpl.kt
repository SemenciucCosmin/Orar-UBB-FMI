package com.ubb.fmi.orar.data.notifications.datasource

import platform.UserNotifications.UNUserNotificationCenter

class NotificationCacheDataSourceImpl: NotificationCacheDataSource {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()

    override fun getNotificationCenter(): UNUserNotificationCenter {
        return notificationCenter
    }
}
