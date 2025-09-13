package com.ubb.fmi.orar.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

/**
 * Android platform specific implementation of factory for creating [DataStore] instance
 */
actual class DataStoreFactory(private val context: Context) {

    /**
     * Creates a [DataStore] instance for storing preferences.
     * The preferences are stored in a file with [name] in the application's files directory.
     */
    actual fun create(name: String): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                context.filesDir.resolve(name + PREFERENCES_FILE_EXTENSION).absolutePath.toPath()
            }
        )
    }

    companion object {
        private const val PREFERENCES_FILE_EXTENSION = ".preferences_pb"
    }
}