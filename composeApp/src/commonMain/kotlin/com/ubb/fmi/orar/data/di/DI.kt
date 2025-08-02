package com.ubb.fmi.orar.data.di

import com.ubb.fmi.orar.data.TestViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun timetableDataModule() = module {
    viewModelOf(::TestViewModel)
}
