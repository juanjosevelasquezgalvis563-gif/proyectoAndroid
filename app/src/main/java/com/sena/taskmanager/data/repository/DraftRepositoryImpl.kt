package com.sena.taskmanager.data.repository

import com.sena.taskmanager.data.local.dao.TaskDraftDao
import com.sena.taskmanager.data.local.entity.TaskDraftEntity
import com.sena.taskmanager.domain.model.TaskDraft
import com.sena.taskmanager.domain.repository.DraftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DraftRepositoryImpl @Inject constructor(
    private val dao: TaskDraftDao
) : DraftRepository {

    override fun getDrafts(
        ownerId: String
    ): Flow<List<TaskDraft>> = flow {

        val drafts = dao.getDraftsByOwner(ownerId)

        emit(
            drafts.map {
                TaskDraft(
                    id = it.id,
                    ownerId = it.ownerId,
                    title = it.title,
                    description = it.description,
                    savedAt = it.savedAt
                )
            }
        )
    }

    override suspend fun saveDraft(
        draft: TaskDraft
    ): Result<Unit> {

        return try {

            dao.insertDraft(
                TaskDraftEntity(
                    id = draft.id,
                    ownerId = draft.ownerId,
                    title = draft.title,
                    description = draft.description,
                    savedAt = draft.savedAt
                )
            )

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun updateDraft(
        draft: TaskDraft
    ): Result<Unit> {

        return try {

            dao.updateDraft(
                TaskDraftEntity(
                    id = draft.id,
                    ownerId = draft.ownerId,
                    title = draft.title,
                    description = draft.description,
                    savedAt = draft.savedAt
                )
            )

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun deleteDraft(
        draft: TaskDraft
    ): Result<Unit> {

        return try {

            dao.deleteDraft(
                TaskDraftEntity(
                    id = draft.id,
                    ownerId = draft.ownerId,
                    title = draft.title,
                    description = draft.description,
                    savedAt = draft.savedAt
                )
            )

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}