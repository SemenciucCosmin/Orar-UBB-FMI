package com.ubb.fmi.orar.domain.studylines.di

import com.ubb.fmi.orar.domain.studylines.usecase.GetStudyLineTimetableUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun studyLinesDomainModule() = module {
    factoryOf(::GetStudyLineTimetableUseCase)
}
