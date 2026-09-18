package com.sena.taskmanager.domain.usecase.task

import com.sena.taskmanager.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(
        taskId: String
    ): Result<Unit> {

        return repository.deleteTask(taskId)
    }
}