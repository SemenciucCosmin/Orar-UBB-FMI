package com.ubb.fmi.orar.data.notifications.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformNotificationModule(): Module

fun notificationsDataModule() = module {
    includes(platformNotificationModule())
}
