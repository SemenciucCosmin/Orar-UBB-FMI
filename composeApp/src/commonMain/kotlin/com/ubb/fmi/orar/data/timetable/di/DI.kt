package com.ubb.fmi.orar.data.timetable.di

import com.ubb.fmi.orar.data.timetable.TestViewModel
import com.ubb.fmi.orar.data.timetable.api.TimetableApi
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun timetableDataModule() = module {
    factory<TimetableApi> { TimetableApi(get()) }
    viewModelOf(::TestViewModel)
}
