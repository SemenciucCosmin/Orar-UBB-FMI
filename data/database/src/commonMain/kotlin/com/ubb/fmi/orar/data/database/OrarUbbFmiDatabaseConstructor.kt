package com.ubb.fmi.orar.data.database

import androidx.room.RoomDatabaseConstructor

/**
 * Factory with platform specific implementations for creating a database instance
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object OrarUbbFmiDatabaseConstructor : RoomDatabaseConstructor<OrarUbbFmiDatabase> {

    /**
     * Creates a [OrarUbbFmiDatabase] instance
     */
    override fun initialize(): OrarUbbFmiDatabase
}
