package com.ubb.fmi.orar.domain.notifications.di

import com.ubb.fmi.orar.domain.notifications.usecase.ChangeEventNotificationUseCase
import com.ubb.fmi.orar.domain.notifications.usecase.InitializeTimetableNotificationsUseCase
import com.ubb.fmi.orar.domain.notifications.usecase.InvalidateTimetableNotificationsUseCase
import com.ubb.fmi.orar.domain.notifications.usecase.RescheduleCachedNotificationsUseCase
import com.ubb.fmi.orar.domain.notifications.usecase.ScheduleEventNotificationsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun notificationsDomainModule() = module {
    factoryOf(::InitializeTimetableNotificationsUseCase)
    factoryOf(::InvalidateTimetableNotificationsUseCase)
    factoryOf(::ScheduleEventNotificationsUseCase)
    factoryOf(::ChangeEventNotificationUseCase)
    factoryOf(::RescheduleCachedNotificationsUseCase)
}
