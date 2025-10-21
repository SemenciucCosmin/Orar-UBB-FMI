package com.ubb.fmi.orar.feature.news.di

import com.ubb.fmi.orar.feature.news.ui.viewmodel.NewsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the news feature.
 */
fun newsFeatureModule() = module {
    viewModelOf(::NewsViewModel)
}
