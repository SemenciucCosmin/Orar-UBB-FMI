package com.ubb.fmi.orar.di

import com.ubb.fmi.orar.network.di.networkDataModule
import com.ubb.fmi.orar.data.di.timetableDataModule
import com.ubb.fmi.orar.data.rooms.di.roomsDataModule
import com.ubb.fmi.orar.data.studyline.di.studyLineDataModule
import com.ubb.fmi.orar.data.subjects.di.subjectsDataModule
import com.ubb.fmi.orar.data.teachers.di.teachersDataModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

fun commonModule() = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    includes(networkDataModule())
    includes(timetableDataModule())
    includes(teachersDataModule())
    includes(roomsDataModule())
    includes(studyLineDataModule())
    includes(subjectsDataModule())
}
