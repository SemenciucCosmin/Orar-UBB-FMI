package com.ubb.fmi.orar.data.permissions.di

import com.ubb.fmi.orar.data.permissions.repository.PermissionRepository
import com.ubb.fmi.orar.data.permissions.repository.PermissionRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformPermissionModule(): Module = module {
    factory<PermissionRepository> { PermissionRepositoryImpl() }
}
