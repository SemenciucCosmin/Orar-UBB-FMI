package com.ubb.fmi.orar.data.feedback.di

import com.ubb.fmi.orar.data.feedback.preferences.FeedbackPreferences
import com.ubb.fmi.orar.data.feedback.preferences.FeedbackPreferencesImpl
import com.ubb.fmi.orar.data.preferences.factory.DataStoreFactory
import org.koin.dsl.module

/**
 * Provides the Koin module for feedback data layer.
 */
fun feedbackDataModule() = module {
    single<FeedbackPreferences> {
        FeedbackPreferencesImpl(
            get<DataStoreFactory>().create(FeedbackPreferences.PREFERENCES_NAME)
        )
    }
}
