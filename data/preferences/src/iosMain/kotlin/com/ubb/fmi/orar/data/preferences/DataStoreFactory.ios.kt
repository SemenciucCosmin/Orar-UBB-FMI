package com.ubb.fmi.orar.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * Ios platform specific implementation of factory for creating [DataStore] instance
 */
actual class DataStoreFactory {

    /**
     * Creates a [DataStore] instance for storing preferences.
     * The preferences are stored in a file named [PREFERENCES_NAME] in the application's documents directory.
     */
    @OptIn(ExperimentalForeignApi::class)
    actual fun create(name: String): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                val documentDirectory: NSURL? = NSFileManager.Companion.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )

                (requireNotNull(documentDirectory).path + "/$name$PREFERENCES_FILE_EXTENSION").toPath()
            }
        )
    }

    companion object {
        private const val PREFERENCES_FILE_EXTENSION = ".preferences_pb"
    }
}