package com.ubb.fmi.orar.feature.groups.di

import com.ubb.fmi.orar.feature.groups.ui.viewmodel.GroupsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun groupsFeatureModule() = module {
    viewModelOf(::GroupsViewModel)
}
