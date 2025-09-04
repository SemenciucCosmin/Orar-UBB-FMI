package com.ubb.fmi.orar.data.preferences.di

import org.koin.core.module.Module

/**
 * Provides the platform-specific Koin module for preferences data operations.
 * This module includes the DataStoreFactory for managing preferences.
 */
expect fun preferencesDataModule(): Module

