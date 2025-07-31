package com.ubb.fmi.orar.di

import com.ubb.fmi.orar.network.di.networkDataModule
import com.ubb.fmi.orar.data.timetable.di.timetableDataModule
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

fun commonModule() = module {
    includes(networkDataModule())
    includes(timetableDataModule())
}
