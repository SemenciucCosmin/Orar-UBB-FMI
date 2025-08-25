package com.ubb.fmi.orar.feature.usertimetable.di

import com.ubb.fmi.orar.feature.usertimetable.ui.viewmodel.UserTimetableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun userTimetableFeatureModule() = module {
    viewModelOf(::UserTimetableViewModel)
}
