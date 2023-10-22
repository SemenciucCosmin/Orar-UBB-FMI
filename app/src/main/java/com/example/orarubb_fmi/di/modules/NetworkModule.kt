package com.example.orarubb_fmi.di.modules

import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://www.cs.ubbcluj.ro/files/orar/")
            .build()
    }
}
