package com.ubb.fmi.orar.data.database

import androidx.room.RoomDatabase

/**
 * Factory with platform specific implementations for creating a database instance
 */
expect class DatabaseFactory {

    /**
     * Creates a [OrarUbbFmiDatabase] instance
     */
    fun create(): RoomDatabase.Builder<OrarUbbFmiDatabase>
}
