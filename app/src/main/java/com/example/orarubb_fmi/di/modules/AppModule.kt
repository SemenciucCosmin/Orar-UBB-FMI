package com.example.orarubb_fmi.di.modules

import com.example.orarubb_fmi.datasource.api.TimetableApiService
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    factory { get<Retrofit>().create(TimetableApiService::class.java) }
}
