package com.sena.taskmanager.domain.usecase.draft

import com.sena.taskmanager.domain.model.TaskDraft
import com.sena.taskmanager.domain.repository.DraftRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDraftsUseCase @Inject constructor(
    private val repository: DraftRepository
) {

    operator fun invoke(
        ownerId: String
    ): Flow<List<TaskDraft>> {
        return repository.getDrafts(ownerId)
    }
}