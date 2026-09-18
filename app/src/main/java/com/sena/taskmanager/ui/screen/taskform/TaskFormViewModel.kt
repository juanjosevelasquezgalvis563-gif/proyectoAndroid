package com.sena.taskmanager.ui.screen.taskform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sena.taskmanager.domain.model.Task
import com.sena.taskmanager.domain.usecase.auth.GetCurrentUserUseCase
import com.sena.taskmanager.domain.usecase.draft.SaveDraftUseCase
import com.sena.taskmanager.domain.usecase.task.CreateTaskUseCase
import com.sena.taskmanager.domain.usecase.task.UpdateTaskUseCase
import com.sena.taskmanager.ui.state.OperationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val saveDraftUseCase: SaveDraftUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _operationState =
        MutableStateFlow<OperationState>(OperationState.Idle)

    val operationState: StateFlow<OperationState> =
        _operationState.asStateFlow()

    fun saveTask(
        id: String,
        title: String,
        description: String,
        completed: Boolean,
        createdAt: Long
    ) {

        val userId = getCurrentUserUseCase()

        if (userId == null) {
            _operationState.value =
                OperationState.Error("No hay una sesión activa.")
            return
        }

        if (title.isBlank()) {
            _operationState.value =
                OperationState.Error("El título es obligatorio.")
            return
        }

        val now = System.currentTimeMillis()

        viewModelScope.launch {

            _operationState.value = OperationState.Loading

            val result = if (id.isBlank()) {

                val task = Task(
                    ownerId = userId,
                    title = title.trim(),
                    description = description.trim(),
                    completed = completed,
                    createdAt = now,
                    updatedAt = now
                )

                createTaskUseCase(task)

            } else {

                val task = Task(
                    id = id,
                    ownerId = userId,
                    title = title.trim(),
                    description = description.trim(),
                    completed = completed,
                    createdAt = createdAt,
                    updatedAt = now
                )

                updateTaskUseCase(task)
            }

            result
                .onSuccess {
                    _operationState.value =
                        OperationState.Success
                }
                .onFailure { error ->

                    _operationState.value =
                        OperationState.Error(
                            error.message
                                ?: "No se pudo guardar la tarea."
                        )
                }
        }
    }

    fun saveDraft(
        title: String,
        description: String
    ) {

        val userId = getCurrentUserUseCase()

        if (userId == null) {
            _operationState.value =
                OperationState.Error("No hay una sesión activa.")
            return
        }

        if (title.isBlank()) {
            _operationState.value =
                OperationState.Error("El título es obligatorio.")
            return
        }

        val draft = com.sena.taskmanager.domain.model.TaskDraft(
            ownerId = userId,
            title = title.trim(),
            description = description.trim(),
            savedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {

            _operationState.value = OperationState.Loading

            saveDraftUseCase(draft)
                .onSuccess {
                    _operationState.value =
                        OperationState.Success
                }
                .onFailure { error ->

                    _operationState.value =
                        OperationState.Error(
                            error.message
                                ?: "No se pudo guardar el borrador."
                        )
                }
        }
    }
}