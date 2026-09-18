package com.sena.taskmanager.domain.repository

import com.sena.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getTasks(
        ownerId: String
    ): Flow<Result<List<Task>>>

    suspend fun createTask(
        task: Task
    ): Result<Unit>

    suspend fun updateTask(
        task: Task
    ): Result<Unit>

    suspend fun deleteTask(
        taskId: String
    ): Result<Unit>
}