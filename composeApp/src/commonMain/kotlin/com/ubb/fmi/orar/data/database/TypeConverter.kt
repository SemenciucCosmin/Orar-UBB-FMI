package com.ubb.fmi.orar.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

object TypeConverter {
    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
}
