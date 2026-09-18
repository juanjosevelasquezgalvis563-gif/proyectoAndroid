package com.sena.taskmanager.domain.usecase.draft


import com.sena.taskmanager.domain.model.Task
import com.sena.taskmanager.domain.model.TaskDraft
import com.sena.taskmanager.domain.repository.DraftRepository
import com.sena.taskmanager.domain.repository.TaskRepository
import javax.inject.Inject

class PublishDraftUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val draftRepository: DraftRepository
) {

    suspend operator fun invoke(
        draft: TaskDraft
    ): Result<Unit> {

        val now = System.currentTimeMillis()

        val task = Task(
            id = "",
            ownerId = draft.ownerId,
            title = draft.title,
            description = draft.description,
            completed = false,
            createdAt = now,
            updatedAt = now
        )

        val publishResult = taskRepository.createTask(task)

        if (publishResult.isFailure) {
            return Result.failure(
                publishResult.exceptionOrNull()
                    ?: Exception("No se pudo publicar el borrador")
            )
        }

        val deleteResult = draftRepository.deleteDraft(draft)

        if (deleteResult.isFailure) {
            return Result.failure(
                deleteResult.exceptionOrNull()
                    ?: Exception("La tarea fue publicada, pero no se pudo eliminar el borrador")
            )
        }

        return Result.success(Unit)
    }
}