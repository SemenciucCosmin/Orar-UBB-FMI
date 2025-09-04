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
     * The preferences are stored in a file named [PREFERENCES_NAME] in the application's files directory.
     */
    actual fun create(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                context.filesDir.resolve(PREFERENCES_NAME).absolutePath.toPath()
            }
        )
    }

    actual companion object {
        internal actual const val PREFERENCES_NAME = "dice.preferences_pb"
    }
}