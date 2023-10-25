package com.example.orarubb_fmi.di.modules

import androidx.room.Room
import com.example.orarubb_fmi.common.DATABASE_FILE
import com.example.orarubb_fmi.data.database.TimetableDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), TimetableDatabase::class.java, DATABASE_FILE).build()
    }
    factory { get<TimetableDatabase>().timetableDao() }
}
