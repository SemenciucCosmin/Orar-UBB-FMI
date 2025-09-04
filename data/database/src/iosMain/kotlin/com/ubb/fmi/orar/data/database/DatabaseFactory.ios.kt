package com.ubb.fmi.orar.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * Ios specific implementation for database factory
 */
actual class DatabaseFactory {

    /**
     * Creates a [OrarUbbFmiDatabase] instance
     */
    actual fun create(): RoomDatabase.Builder<OrarUbbFmiDatabase> {
        val dbFile = documentDirectory() + "/${OrarUbbFmiDatabase.DATABASE_NAME}"
        return Room.databaseBuilder<OrarUbbFmiDatabase>(
            name = dbFile
        )
    }

    /**
     * Returns the path to the document directory on iOS.
     * This is where the database file will be stored.
     */
    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        return requireNotNull(documentDirectory?.path)
    }
}