package com.sena.taskmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sena.taskmanager.data.local.entity.TaskDraftEntity

@Dao
interface TaskDraftDao {

    @Insert
    suspend fun insertDraft(
        draft: TaskDraftEntity
    ): Long

    @Query(
        "SELECT * FROM task_drafts " +
                "WHERE ownerId = :ownerId " +
                "ORDER BY savedAt DESC"
    )
    suspend fun getDraftsByOwner(
        ownerId: String
    ): List<TaskDraftEntity>


    @Update
    suspend fun updateDraft(
        draft: TaskDraftEntity
    )


    @Delete
    suspend fun deleteDraft(
        draft: TaskDraftEntity
    )
}