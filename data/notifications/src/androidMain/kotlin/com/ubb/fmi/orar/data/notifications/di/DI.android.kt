package com.ubb.fmi.orar.data.notifications.di

import com.ubb.fmi.orar.data.notifications.datasource.NotificationCacheDataSource
import com.ubb.fmi.orar.data.notifications.datasource.NotificationCacheDataSourceImpl
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformNotificationModule(): Module = module {
    single<NotificationCacheDataSource> { NotificationCacheDataSourceImpl(get()) }
    factory<NotificationRepository> { NotificationRepositoryImpl(get(), get(), get(), get(), get()) }
}
