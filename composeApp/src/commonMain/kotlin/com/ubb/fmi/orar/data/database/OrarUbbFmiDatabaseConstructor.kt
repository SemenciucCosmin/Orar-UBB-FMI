package com.ubb.fmi.orar.data.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object OrarUbbFmiDatabaseConstructor: RoomDatabaseConstructor<OrarUbbFmiDatabase> {

    override fun initialize(): OrarUbbFmiDatabase

}
