package com.ubb.fmi.orar.data.notifications.datasource

import platform.UserNotifications.UNUserNotificationCenter

interface NotificationCacheDataSource {

    fun getNotificationCenter(): UNUserNotificationCenter
}
