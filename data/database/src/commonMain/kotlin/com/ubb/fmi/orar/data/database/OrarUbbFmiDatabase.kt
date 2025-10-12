package com.ubb.fmi.orar.data.database

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.ubb.fmi.orar.data.database.dao.EventDao
import com.ubb.fmi.orar.data.database.dao.GroupDao
import com.ubb.fmi.orar.data.database.dao.RoomDao
import com.ubb.fmi.orar.data.database.dao.StudyLineDao
import com.ubb.fmi.orar.data.database.dao.SubjectDao
import com.ubb.fmi.orar.data.database.dao.TeacherDao
import com.ubb.fmi.orar.data.database.model.EventEntity
import com.ubb.fmi.orar.data.database.model.GroupEntity
import com.ubb.fmi.orar.data.database.model.RoomEntity
import com.ubb.fmi.orar.data.database.model.StudyLineEntity
import com.ubb.fmi.orar.data.database.model.SubjectEntity
import com.ubb.fmi.orar.data.database.model.TeacherEntity

/**
 * Database class for Orar UBB FMI application.
 * This class defines the database schema and serves as the main access point for the underlying SQLite database.
 * It includes entities for rooms, study lines, subjects, teachers, and timetable classes.
 */
@Database(
    version = 2,
    entities = [
        EventEntity::class,
        GroupEntity::class,
        RoomEntity::class,
        StudyLineEntity::class,
        SubjectEntity::class,
        TeacherEntity::class,
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = AutoMigrations.AutoMigrationSpec1To2::class),
    ]
)
@ConstructedBy(OrarUbbFmiDatabaseConstructor::class)
abstract class OrarUbbFmiDatabase : RoomDatabase() {

    abstract val eventDao: EventDao

    abstract val groupDao: GroupDao

    abstract val roomDao: RoomDao

    abstract val studyLineDao: StudyLineDao

    abstract val subjectDao: SubjectDao

    abstract val teacherDao: TeacherDao

    companion object {
        const val DATABASE_NAME = "orar_ubb_fmi.db"
    }
}