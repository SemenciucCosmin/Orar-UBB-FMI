package com.ubb.fmi.orar.domain.announcements.usecase

import com.ubb.fmi.orar.data.announcements.preferences.AnnouncementsPreferences

/**
 * Use case for marking that the update announcement was shown
 */
class SetUpdateAnnouncementShownUseCase(
    private val announcementsPreferences: AnnouncementsPreferences
) {

    suspend operator fun invoke() {
        announcementsPreferences.setUpdateAnnouncementShown()
    }
}