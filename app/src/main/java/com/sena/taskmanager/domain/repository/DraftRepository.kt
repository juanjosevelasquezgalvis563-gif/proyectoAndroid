package com.sena.taskmanager.domain.repository

import com.sena.taskmanager.domain.model.TaskDraft
import kotlinx.coroutines.flow.Flow

interface DraftRepository {

    fun getDrafts(
        ownerId: String
    ): Flow<List<TaskDraft>>

    suspend fun saveDraft(
        draft: TaskDraft
    ): Result<Unit>

    suspend fun updateDraft(
        draft: TaskDraft
    ): Result<Unit>

    suspend fun deleteDraft(
        draft: TaskDraft
    ): Result<Unit>
}