package com.ubb.fmi.orar.data.permissions.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformPermissionModule(): Module

fun permissionsDataModule() = module {
    includes(platformPermissionModule())
}
