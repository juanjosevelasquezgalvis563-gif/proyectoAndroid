package com.sena.taskmanager.domain.usecase.task

import com.sena.taskmanager.domain.model.Task
import com.sena.taskmanager.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(
        task: Task
    ): Result<Unit> {

        return repository.updateTask(task)
    }
}