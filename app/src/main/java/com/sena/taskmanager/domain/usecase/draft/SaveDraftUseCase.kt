package com.sena.taskmanager.domain.usecase.draft


import com.sena.taskmanager.domain.model.TaskDraft
import com.sena.taskmanager.domain.repository.DraftRepository
import javax.inject.Inject

class SaveDraftUseCase @Inject constructor(
    private val draftRepository: DraftRepository
) {
    suspend operator fun invoke(draft: TaskDraft) =
        draftRepository.saveDraft(draft)
}