package com.ubb.fmi.orar.feature.notifications.di

import com.ubb.fmi.orar.feature.notifications.ui.viewmodel.NotificationsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the notifications settings feature.
 */
fun notificationsFeatureModule() = module {
    viewModelOf(::NotificationsViewModel)
}
