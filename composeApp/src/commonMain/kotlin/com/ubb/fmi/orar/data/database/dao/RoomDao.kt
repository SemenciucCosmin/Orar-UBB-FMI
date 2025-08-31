package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ubb.fmi.orar.data.database.model.RoomEntity

@Dao
interface RoomDao {
    @Transaction
    @Query("SELECT * FROM rooms WHERE configurationId LIKE :configurationId")
    suspend fun getAll(configurationId: String): List<RoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RoomEntity)

    @Query("DELETE FROM rooms WHERE configurationId LIKE :configurationId")
    suspend fun deleteAll(configurationId: String)
}
