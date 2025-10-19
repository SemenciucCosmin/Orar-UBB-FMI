package com.ubb.fmi.orar.di

import com.ubb.fmi.orar.data.database.di.databaseDataModule
import com.ubb.fmi.orar.data.groups.di.groupsDataModule
import com.ubb.fmi.orar.data.network.di.networkDataModule
import com.ubb.fmi.orar.data.preferences.di.preferencesDataModule
import com.ubb.fmi.orar.data.rooms.di.roomsDataModule
import com.ubb.fmi.orar.data.settings.di.settingsDataModule
import com.ubb.fmi.orar.data.studylines.di.studyLinesDataModule
import com.ubb.fmi.orar.data.subjects.di.subjectsDataModule
import com.ubb.fmi.orar.data.teachers.di.teachersDataModule
import com.ubb.fmi.orar.data.timetable.di.timetableDataModule
import com.ubb.fmi.orar.domain.logging.di.loggingDomainModule
import com.ubb.fmi.orar.domain.theme.di.themeDomainModule
import com.ubb.fmi.orar.domain.timetable.di.timetableDomainModule
import com.ubb.fmi.orar.domain.usertimetable.di.userTimetableDomainModule
import com.ubb.fmi.orar.feature.form.di.formFeatureModule
import com.ubb.fmi.orar.feature.groups.di.groupsFeatureModule
import com.ubb.fmi.orar.feature.grouptimetable.di.groupsTimetableFeatureModule
import com.ubb.fmi.orar.feature.rooms.di.roomsFeatureModule
import com.ubb.fmi.orar.feature.roomtimetable.di.roomTimetableFeatureModule
import com.ubb.fmi.orar.feature.settings.di.settingsFeatureModule
import com.ubb.fmi.orar.feature.startup.di.startupFeatureModule
import com.ubb.fmi.orar.feature.studylines.di.studyLinesFeatureModule
import com.ubb.fmi.orar.feature.subjects.di.subjectsFeatureModule
import com.ubb.fmi.orar.feature.subjectstimetable.di.subjectTimetableFeatureModule
import com.ubb.fmi.orar.feature.teachers.di.teachersFeatureModule
import com.ubb.fmi.orar.feature.teachertimetable.di.teacherTimetableFeatureModule
import com.ubb.fmi.orar.feature.usertimetable.di.userTimetableFeatureModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

/**
 * Provides the common Koin module for the Orar UBB FMI application.
 * This module includes all necessary dependencies for the application to function correctly.
 */
fun commonModule() = module {
    // COMMON
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    // DATABASE
    includes(databaseDataModule())

    // FORM
    includes(formFeatureModule())

    // LOGGING
    includes(loggingDomainModule())

    // GROUPS
    includes(groupsDataModule())
    includes(groupsFeatureModule())
    includes(groupsTimetableFeatureModule())

    // NETWORK
    includes(networkDataModule())

    // PREFERENCES
    includes(preferencesDataModule())

    // ROOMS
    includes(roomsDataModule())
    includes(roomsFeatureModule())
    includes(roomTimetableFeatureModule())

    // STARTUP
    includes(startupFeatureModule())

    // SETTINGS
    includes(settingsDataModule())
    includes(settingsFeatureModule())

    // STUDY LINES
    includes(studyLinesDataModule())
    includes(studyLinesFeatureModule())

    // SUBJECTS
    includes(subjectsDataModule())
    includes(subjectsFeatureModule())
    includes(subjectTimetableFeatureModule())

    // TEACHERS
    includes(teachersDataModule())
    includes(teachersFeatureModule())
    includes(teacherTimetableFeatureModule())

    // THEME
    includes(themeDomainModule())

    // TIMETABLE
    includes(timetableDataModule())
    includes(timetableDomainModule())
    includes(userTimetableDomainModule())
    includes(userTimetableFeatureModule())
}
