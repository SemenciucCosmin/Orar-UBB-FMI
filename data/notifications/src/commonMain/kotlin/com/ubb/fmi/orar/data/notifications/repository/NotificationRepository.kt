package com.ubb.fmi.orar.data.notifications.repository

import com.ubb.fmi.orar.data.notifications.model.EventNotification

interface NotificationRepository {
    suspend fun schedule(notification: EventNotification)
    suspend fun cancel(id: String)
}
