package com.sena.taskmanager.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sena.taskmanager.data.local.dao.TaskDraftDao
import com.sena.taskmanager.data.local.entity.TaskDraftEntity

@Database(
    entities = [TaskDraftEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDraftDao(): TaskDraftDao
}