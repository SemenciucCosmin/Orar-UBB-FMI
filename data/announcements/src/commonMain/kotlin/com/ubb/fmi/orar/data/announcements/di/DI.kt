package com.ubb.fmi.orar.data.announcements.di

import com.ubb.fmi.orar.data.announcements.preferences.AnnouncementsPreferences
import com.ubb.fmi.orar.data.announcements.preferences.AnnouncementsPreferencesImpl
import com.ubb.fmi.orar.data.preferences.factory.DataStoreFactory
import org.koin.dsl.module

/**
 * Provides the Koin module for announcements data layer.
 */
fun announcementsDataModule() = module {
    single<AnnouncementsPreferences> {
        AnnouncementsPreferencesImpl(
            get<DataStoreFactory>().create(AnnouncementsPreferences.PREFERENCES_NAME)
        )
    }
}
