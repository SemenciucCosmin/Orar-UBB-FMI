package com.ubb.fmi.orar.data.notifications.di

import com.ubb.fmi.orar.data.notifications.preferences.NotificationPreferences
import com.ubb.fmi.orar.data.notifications.preferences.NotificationPreferencesImpl
import com.ubb.fmi.orar.data.preferences.factory.DataStoreFactory
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformNotificationModule(): Module

fun notificationsDataModule() = module {
    single<NotificationPreferences> {
        NotificationPreferencesImpl(
            get<DataStoreFactory>().create(NotificationPreferences.PREFERENCES_NAME)
        )
    }
    includes(platformNotificationModule())
}
