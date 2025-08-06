package com.ubb.fmi.orar.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

actual class DataStoreFactory(private val context: Context) {

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