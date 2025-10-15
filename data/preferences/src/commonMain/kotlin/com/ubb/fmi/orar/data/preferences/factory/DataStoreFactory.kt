package com.ubb.fmi.orar.data.preferences.factory

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Factory with platform specific implementation for creating [androidx.datastore.core.DataStore] instance
 */
expect class DataStoreFactory {

    /**
     * Creates a [androidx.datastore.core.DataStore] instance
     */
    fun create(name: String): DataStore<Preferences>
}