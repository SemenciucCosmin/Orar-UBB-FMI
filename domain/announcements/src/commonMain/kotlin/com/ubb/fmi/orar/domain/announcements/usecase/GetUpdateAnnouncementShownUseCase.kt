package com.ubb.fmi.orar.domain.announcements.usecase

import com.ubb.fmi.orar.data.announcements.preferences.AnnouncementsPreferences
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime

/**
 * Use case for setting the first usage timestamp if not set yet for feedback loop
 */
class GetUpdateAnnouncementShownUseCase(
    private val announcementsPreferences: AnnouncementsPreferences
) {

    @OptIn(ExperimentalTime::class)
    suspend operator fun invoke(): Flow<Boolean> {
        return announcementsPreferences.getUpdateAnnouncementShown()
    }
}