package com.ubb.fmi.orar.feature.studygroups.di

import com.ubb.fmi.orar.feature.studygroups.ui.viewmodel.StudyGroupsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun studyGroupsFeatureModule() = module {
    viewModelOf(::StudyGroupsViewModel)
}
