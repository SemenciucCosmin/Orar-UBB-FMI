package com.ubb.fmi.orar.domain.announcements.usecase

import com.ubb.fmi.orar.data.announcements.preferences.AnnouncementsPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Use case for setting the first usage timestamp if not set yet for feedback loop
 */
class GetUpdateAnnouncementShownUseCase(
    private val announcementsPreferences: AnnouncementsPreferences
) {

    suspend operator fun invoke(): Flow<Boolean> {
        return announcementsPreferences.getUpdateAnnouncementShown()
    }
}