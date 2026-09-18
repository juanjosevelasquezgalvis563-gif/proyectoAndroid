package com.sena.taskmanager.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sena.taskmanager.data.mapper.toDocument
import com.sena.taskmanager.data.mapper.toDomain
import com.sena.taskmanager.domain.model.Task
import com.sena.taskmanager.domain.repository.TaskRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TaskRepository {

    private val tasksCollection =
        firestore.collection("tasks")

    override fun getTasks(
        ownerId: String
    ): Flow<Result<List<Task>>> = callbackFlow {

        val listener = tasksCollection
            .whereEqualTo("ownerId", ownerId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    trySend(
                        Result.failure(
                            Exception("No se pudo obtener la información.")
                        )
                    )
                    return@addSnapshotListener
                }

                val tasks = snapshot.documents.mapNotNull { document ->

                    try {
                        val taskDocument =
                            document.toObject(
                                com.sena.taskmanager.data.remote.model.TaskDocument::class.java
                            )

                        taskDocument?.toDomain(document.id)

                    } catch (e: Exception) {
                        null
                    }
                }

                trySend(Result.success(tasks))
            }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun createTask(
        task: Task
    ): Result<Unit> {

        return try {

            val document =
                tasksCollection.document()

            val taskWithId =
                task.copy(
                    id = document.id
                )

            document
                .set(taskWithId.toDocument())
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun updateTask(
        task: Task
    ): Result<Unit> {

        return try {

            if (task.id.isBlank()) {
                return Result.failure(
                    Exception("El ID de la tarea es obligatorio.")
                )
            }

            tasksCollection
                .document(task.id)
                .set(task.toDocument())
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun deleteTask(
        taskId: String
    ): Result<Unit> {

        return try {

            if (taskId.isBlank()) {
                return Result.failure(
                    Exception("El ID de la tarea es obligatorio.")
                )
            }

            tasksCollection
                .document(taskId)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}