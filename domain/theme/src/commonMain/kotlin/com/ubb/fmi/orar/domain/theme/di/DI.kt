package com.ubb.fmi.orar.domain.theme.di

import com.ubb.fmi.orar.domain.theme.usecase.GetThemeOption
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Provides the Koin module for theme domain operations.
 * This module includes the GetThemeOption for managing theme-related data.
 */
fun themeDomainModule() = module {
    factoryOf(::GetThemeOption)
}
