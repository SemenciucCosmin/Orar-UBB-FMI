package com.ubb.fmi.orar.feature.groups.di

import com.ubb.fmi.orar.feature.groups.ui.viewmodel.GroupsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the Groups feature.
 * This module provides the ViewModel for the Groups screen.
 */
fun groupsFeatureModule() = module {
    viewModelOf(::GroupsViewModel)
}
