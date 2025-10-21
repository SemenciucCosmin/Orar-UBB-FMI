package com.ubb.fmi.orar.data.news.di

import com.ubb.fmi.orar.data.news.datasource.NewsDataSource
import com.ubb.fmi.orar.data.news.datasource.NewsDataSourceImpl
import com.ubb.fmi.orar.data.news.repository.NewsRepository
import com.ubb.fmi.orar.data.news.repository.NewsRepositoryImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for news data layer.
 */
fun newsDataModule() = module {
    factory<NewsDataSource> { NewsDataSourceImpl(get(), get(), get()) }
    single<NewsRepository> { NewsRepositoryImpl(get(), get()) }
}
