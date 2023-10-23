package com.example.orarubb_fmi.di.modules

import com.example.orarubb_fmi.data.datasource.api.TimetableApiService
import com.example.orarubb_fmi.domain.repository.TimetableRepository
import com.example.orarubb_fmi.data.repository.TimetableRepositoryImpl
import com.example.orarubb_fmi.ui.viewmodel.TimetableViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    factory { get<Retrofit>().create(TimetableApiService::class.java) }
    factory<TimetableRepository> { TimetableRepositoryImpl(get()) }
    viewModel { TimetableViewModel(get()) }
}
