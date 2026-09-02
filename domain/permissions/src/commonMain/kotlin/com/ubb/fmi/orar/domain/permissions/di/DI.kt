package com.ubb.fmi.orar.domain.permissions.di

import com.ubb.fmi.orar.domain.permissions.usecase.IsPermissionGrantedUseCase
import com.ubb.fmi.orar.domain.permissions.usecase.RequestPermissionUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun permissionsDomainModule() = module {
    factoryOf(::IsPermissionGrantedUseCase)
    factoryOf(::RequestPermissionUseCase)
}
