package com.ubb.fmi.orar.data.announcements.preferences

import kotlinx.coroutines.flow.Flow

/**
 * Preferences for announcements feature
 */
interface AnnouncementsPreferences {

    /**
     * Gets whether the update announcement was shown
     */
    suspend fun getUpdateAnnouncementShown(): Flow<Boolean>

    /**
     * Marks update announcement as shown
     */
    suspend fun setUpdateAnnouncementShown()

    companion object {
        const val PREFERENCES_NAME = "ANNOUNCEMENTS_PREFERENCES"
    }
}
