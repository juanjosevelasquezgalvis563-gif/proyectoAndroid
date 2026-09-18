package com.sena.taskmanager.di

import android.content.Context
import androidx.room.Room
import com.sena.taskmanager.data.local.dao.TaskDraftDao
import com.sena.taskmanager.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "task_manager_database"
        ).build()
    }

    @Provides
    fun provideTaskDraftDao(
        database: AppDatabase
    ): TaskDraftDao {
        return database.taskDraftDao()
    }
}

