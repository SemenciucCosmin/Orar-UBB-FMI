package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ubb.fmi.orar.data.database.di.Car
import com.ubb.fmi.orar.data.database.model.RoomClassEntity
import com.ubb.fmi.orar.data.database.model.RoomEntity
import com.ubb.fmi.orar.data.database.model.RoomWithClasses
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Transaction
    @Query("SELECT * FROM rooms")
    suspend fun getAllRoomsWithClasses(): List<RoomWithClasses>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomClasses(roomClasses: List<RoomClassEntity>)
}
