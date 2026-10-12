package com.ubb.fmi.orar.domain.colorpack.di

import com.ubb.fmi.orar.domain.colorpack.usecase.GetColorPackUseCase
import com.ubb.fmi.orar.domain.colorpack.usecase.ResetItemColorUseCase
import com.ubb.fmi.orar.domain.colorpack.usecase.SetItemColorUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Provides the Koin module for color pack domain operations: reading, saving and resetting custom colors.
 */
fun colorPackDomainModule() = module {
    factoryOf(::GetColorPackUseCase)
    factoryOf(::SetItemColorUseCase)
    factoryOf(::ResetItemColorUseCase)
}
