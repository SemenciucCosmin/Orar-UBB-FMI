package com.ubb.fmi.orar.di

import com.ubb.fmi.orar.data.TestViewModel
import com.ubb.fmi.orar.data.database.di.databaseDataModule
import com.ubb.fmi.orar.network.di.networkDataModule
import com.ubb.fmi.orar.data.rooms.di.roomsDataModule
import com.ubb.fmi.orar.data.studyline.di.studyLineDataModule
import com.ubb.fmi.orar.data.subjects.di.subjectsDataModule
import com.ubb.fmi.orar.data.teachers.di.teachersDataModule
import com.ubb.fmi.orar.data.timetables.di.timetablesDataModule
import com.ubb.fmi.orar.feature.form.di.formFeatureModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect fun platformModule(): Module

fun commonModule() = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    viewModelOf(::TestViewModel)
    includes(networkDataModule())
    includes(roomsDataModule())
    includes(studyLineDataModule())
    includes(subjectsDataModule())
    includes(teachersDataModule())
    includes(timetablesDataModule())
    includes(formFeatureModule())
    includes(databaseDataModule())
}
