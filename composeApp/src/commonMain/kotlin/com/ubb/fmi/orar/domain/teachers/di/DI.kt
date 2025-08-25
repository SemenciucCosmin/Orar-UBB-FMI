package com.ubb.fmi.orar.domain.teachers.di

import com.ubb.fmi.orar.domain.teachers.usecase.GetTeacherTimetableUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun teachersDomainModule() = module {
    factoryOf(::GetTeacherTimetableUseCase)
}
