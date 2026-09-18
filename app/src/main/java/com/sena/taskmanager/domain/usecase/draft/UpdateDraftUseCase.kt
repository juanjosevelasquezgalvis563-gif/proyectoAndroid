package com.sena.taskmanager.domain.usecase.draft

import com.sena.taskmanager.domain.model.TaskDraft
import com.sena.taskmanager.domain.repository.DraftRepository
import javax.inject.Inject

class UpdateDraftUseCase @Inject constructor(
    private val repository: DraftRepository
) {

    suspend operator fun invoke(
        draft: TaskDraft
    ): Result<Unit> {
        return repository.updateDraft(draft)
    }
}