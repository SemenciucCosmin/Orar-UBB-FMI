package com.ubb.fmi.orar.domain.announcements.di

import com.ubb.fmi.orar.domain.announcements.usecase.GetUpdateAnnouncementShownUseCase
import com.ubb.fmi.orar.domain.announcements.usecase.SetUpdateAnnouncementShownUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Provides the platform-specific Koin module for announcement domain layer
 */
fun announcementsDomainModule() = module {
    factoryOf(::GetUpdateAnnouncementShownUseCase)
    factoryOf(::SetUpdateAnnouncementShownUseCase)
}
