package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ubb.fmi.orar.data.database.model.RoomClassEntity
import com.ubb.fmi.orar.data.database.model.RoomEntity
import com.ubb.fmi.orar.data.rooms.model.RoomClass

@Dao
interface RoomClassDao {
    @Transaction
    @Query("SELECT * FROM room_classes")
    suspend fun getRoomsClasses(): List<RoomClassEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomClass(roomClassEntity: RoomClassEntity)
}
