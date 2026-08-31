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

    companion object {
        const val PREFERENCES_NAME = "NOTIFICATION_PREFERENCES"
    }
}
