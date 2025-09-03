package com.ubb.fmi.orar.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {

    fun create(): RoomDatabase.Builder<OrarUbbFmiDatabase>
}
