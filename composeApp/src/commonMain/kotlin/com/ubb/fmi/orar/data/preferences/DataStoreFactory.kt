package com.ubb.fmi.orar.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class DataStoreFactory {

    fun create(): DataStore<Preferences>

    companion object {
        internal val PREFERENCES_NAME: String
    }
}