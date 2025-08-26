package com.ubb.fmi.orar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ubb.fmi.orar.data.database.model.RoomClassEntity
import com.ubb.fmi.orar.data.database.model.SubjectClassEntity

@Dao
interface RoomClassDao {

    @Query("SELECT * FROM room_classes")
    suspend fun getAllRoomsClasses(): List<RoomClassEntity>

    @Query("SELECT * FROM room_classes WHERE roomId LIKE :roomId")
    suspend fun getRoomClasses(roomId: String): List<RoomClassEntity>

    @Query("SELECT * FROM room_classes WHERE id LIKE :classId")
    suspend fun getRoomClass(classId: String): RoomClassEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomClass(roomClassEntity: RoomClassEntity)
}
