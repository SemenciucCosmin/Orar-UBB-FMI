package com.ubb.fmi.orar.data.notifications.repository

import com.ubb.fmi.orar.data.notifications.model.ClassNotification

interface NotificationRepository {
    suspend fun schedule(notification: ClassNotification)
    suspend fun cancel(id: String)
    suspend fun cancelAll()
    suspend fun getScheduled(): List<ClassNotification>
}
