package com.ubb.fmi.orar.domain.subjects.di

import com.ubb.fmi.orar.domain.subjects.usecase.GetSubjectTimetableUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun subjectsDomainModule() = module {
    factoryOf(::GetSubjectTimetableUseCase)
}
