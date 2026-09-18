package com.sena.taskmanager.domain.usecase.task

import com.sena.taskmanager.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    operator fun invoke(
        ownerId: String
    ) = repository.getTasks(ownerId)
}