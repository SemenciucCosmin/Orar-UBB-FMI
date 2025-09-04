package com.ubb.fmi.orar.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Factory with platform specific implementation for creating [DataStore] instance
 */
expect class DataStoreFactory {

    /**
     * Creates a [DataStore] instance
     */
    fun create(): DataStore<Preferences>

    companion object {
        internal val PREFERENCES_NAME: String
    }
}