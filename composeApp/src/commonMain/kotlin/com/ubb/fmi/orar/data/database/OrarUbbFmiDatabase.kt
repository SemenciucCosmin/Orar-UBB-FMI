package com.ubb.fmi.orar.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ubb.fmi.orar.data.database.dao.RoomDao
import com.ubb.fmi.orar.data.database.dao.StudyLineClassDao
import com.ubb.fmi.orar.data.database.dao.StudyLineDao
import com.ubb.fmi.orar.data.database.dao.SubjectDao
import com.ubb.fmi.orar.data.database.dao.TeacherDao
import com.ubb.fmi.orar.data.database.model.RoomClassEntity
import com.ubb.fmi.orar.data.database.model.RoomEntity
import com.ubb.fmi.orar.data.database.model.StudyLineClassEntity
import com.ubb.fmi.orar.data.database.model.StudyLineEntity
import com.ubb.fmi.orar.data.database.model.SubjectClassEntity
import com.ubb.fmi.orar.data.database.model.SubjectEntity
import com.ubb.fmi.orar.data.database.model.TeacherClassEntity
import com.ubb.fmi.orar.data.database.model.TeacherEntity

@Database(
    version = 1,
    entities = [
        RoomEntity::class,
        RoomClassEntity::class,
        StudyLineEntity::class,
        StudyLineClassEntity::class,
        SubjectEntity::class,
        SubjectClassEntity::class,
        TeacherEntity::class,
        TeacherClassEntity::class,
    ],
)
@TypeConverters(TypeConverter::class)
@ConstructedBy(OrarUbbFmiDatabaseConstructor::class)
abstract class OrarUbbFmiDatabase : RoomDatabase() {

    abstract val roomDao: RoomDao

    abstract val studyLineDao: StudyLineDao

    abstract val studyLineClassDao: StudyLineClassDao

    abstract val subjectDao: SubjectDao

    abstract val teacherDao: TeacherDao

    companion object {
        const val DATABASE_NAME = "orar_ubb_fmi.db"
    }
}