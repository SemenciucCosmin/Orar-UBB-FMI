package com.ubb.fmi.orar.di

import com.ubb.fmi.orar.data.database.di.databaseDataModule
import com.ubb.fmi.orar.data.preferences.di.preferencesDataModule
import com.ubb.fmi.orar.data.network.di.networkDataModule
import com.ubb.fmi.orar.data.rooms.di.roomsDataModule
import com.ubb.fmi.orar.data.studyline.di.studyLineDataModule
import com.ubb.fmi.orar.data.subjects.di.subjectsDataModule
import com.ubb.fmi.orar.data.teachers.di.teachersDataModule
import com.ubb.fmi.orar.data.timetables.di.timetablesDataModule
import com.ubb.fmi.orar.domain.rooms.di.roomsDomainModule
import com.ubb.fmi.orar.domain.studylines.di.studyLinesDomainModule
import com.ubb.fmi.orar.domain.subjects.di.subjectsDomainModule
import com.ubb.fmi.orar.domain.teachers.di.teachersDomainModule
import com.ubb.fmi.orar.domain.timetable.di.timetableDomainModule
import com.ubb.fmi.orar.domain.usertimetable.di.userTimetableDomainModule
import com.ubb.fmi.orar.feature.startup.di.startupFeatureModule
import com.ubb.fmi.orar.feature.form.di.formFeatureModule
import com.ubb.fmi.orar.feature.rooms.di.roomsFeatureModule
import com.ubb.fmi.orar.feature.roomtimetable.di.roomTimetableFeatureModule
import com.ubb.fmi.orar.feature.studygroups.di.studyGroupsFeatureModule
import com.ubb.fmi.orar.feature.studylines.di.studyLinesFeatureModule
import com.ubb.fmi.orar.feature.studylinetimetable.di.studyLineTimetableFeatureModule
import com.ubb.fmi.orar.feature.subjects.di.subjectsFeatureModule
import com.ubb.fmi.orar.feature.subjecttimetable.di.subjectTimetableFeatureModule
import com.ubb.fmi.orar.feature.teachers.di.teachersFeatureModule
import com.ubb.fmi.orar.feature.teachertimetable.di.teacherTimetableFeatureModule
import com.ubb.fmi.orar.feature.usertimetable.di.userTimetableFeatureModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

fun commonModule() = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    includes(databaseDataModule())
    includes(formFeatureModule())
    includes(networkDataModule())
    includes(preferencesDataModule())
    includes(roomsDataModule())
    includes(roomsDomainModule())
    includes(roomsFeatureModule())
    includes(roomTimetableFeatureModule())
    includes(startupFeatureModule())
    includes(studyLineDataModule())
    includes(studyLinesDomainModule())
    includes(studyLinesFeatureModule())
    includes(studyGroupsFeatureModule())
    includes(studyLineTimetableFeatureModule())
    includes(subjectsDataModule())
    includes(subjectsDomainModule())
    includes(subjectsFeatureModule())
    includes(subjectTimetableFeatureModule())
    includes(teachersDataModule())
    includes(teachersDomainModule())
    includes(teachersFeatureModule())
    includes(teacherTimetableFeatureModule())
    includes(timetablesDataModule())
    includes(timetableDomainModule())
    includes(userTimetableDomainModule())
    includes(userTimetableFeatureModule())
}
