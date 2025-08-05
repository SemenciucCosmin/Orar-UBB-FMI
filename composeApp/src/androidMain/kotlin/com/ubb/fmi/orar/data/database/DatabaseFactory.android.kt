package com.ubb.fmi.orar.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(private val context: Context) {

    actual fun create(): RoomDatabase.Builder<OrarUbbFmiDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(OrarUbbFmiDatabase.DATABASE_NAME)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}
