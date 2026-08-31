package com.ubb.fmi.orar.data.notifications.preferences

import kotlinx.coroutines.flow.Flow

/**
 * Preferences for notifications feature
 */
interface NotificationPreferences {

    /**
     * Gets scheduled notification IDs as a [Flow]
     */
    fun getScheduledIds(): Flow<Set<String>>

    /**
     * Adds a notification ID to the scheduled set
     */
    suspend fun addScheduledId(id: String)

    /**
     * Removes a notification ID from the scheduled set
     */
    suspend fun removeScheduledId(id: String)

    /**
     * Clears all scheduled notification IDs
     */
    suspend fun clearScheduledIds()

    /**
     * Returns the serialized data for the given notification ID, or null if not found
     */
    suspend fun getNotificationData(id: String): String?

    /**
     * Stores serialized data for the given notification ID
     */
    suspend fun setNotificationData(id: String, data: String)

    /**
     * Removes stored data for the given notification ID
     */
    suspend fun removeNotificationData(id: String)

    companion object {
        const val PREFERENCES_NAME = "NOTIFICATION_PREFERENCES"
    }
}
