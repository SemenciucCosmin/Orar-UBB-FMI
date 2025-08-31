package com.ubb.fmi.orar.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ubb.fmi.orar.data.database.dao.RoomDao
import com.ubb.fmi.orar.data.database.dao.StudyLineDao
import com.ubb.fmi.orar.data.database.dao.SubjectDao
import com.ubb.fmi.orar.data.database.dao.TeacherDao
import com.ubb.fmi.orar.data.database.dao.TimetableClassDao
import com.ubb.fmi.orar.data.database.model.RoomEntity
import com.ubb.fmi.orar.data.database.model.StudyLineEntity
import com.ubb.fmi.orar.data.database.model.SubjectEntity
import com.ubb.fmi.orar.data.database.model.TeacherEntity
import com.ubb.fmi.orar.data.database.model.TimetableClassEntity

@Database(
    version = 1,
    entities = [
        RoomEntity::class,
        StudyLineEntity::class,
        SubjectEntity::class,
        TeacherEntity::class,
        TimetableClassEntity::class,
    ],
)
@TypeConverters(TypeConverter::class)
@ConstructedBy(OrarUbbFmiDatabaseConstructor::class)
abstract class OrarUbbFmiDatabase : RoomDatabase() {

    abstract val roomDao: RoomDao

    abstract val studyLineDao: StudyLineDao

    abstract val subjectDao: SubjectDao

    abstract val teacherDao: TeacherDao

    abstract val timetableClassDao: TimetableClassDao

    companion object {
        const val DATABASE_NAME = "orar_ubb_fmi.db"
    }
}