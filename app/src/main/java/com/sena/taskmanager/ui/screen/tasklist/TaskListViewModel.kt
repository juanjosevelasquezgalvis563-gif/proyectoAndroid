package com.sena.taskmanager.ui.screen.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sena.taskmanager.domain.model.Task
import com.sena.taskmanager.domain.usecase.auth.GetCurrentUserUseCase
import com.sena.taskmanager.domain.usecase.task.DeleteTaskUseCase
import com.sena.taskmanager.domain.usecase.task.GetTasksUseCase
import com.sena.taskmanager.domain.usecase.task.UpdateTaskUseCase
import com.sena.taskmanager.ui.state.TaskListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    private var tasksJob: Job? = null

    fun loadTasks() {

        val userId = getCurrentUserUseCase()

        if (userId == null) {
            _uiState.value = TaskListUiState(
                errorMessage = "No hay una sesión activa."
            )
            return
        }

        tasksJob?.cancel()

        tasksJob = viewModelScope.launch {

            _uiState.value = TaskListUiState(
                isLoading = true
            )

            getTasksUseCase(userId).collect { result ->

                result
                    .onSuccess { tasks ->
                        _uiState.value = TaskListUiState(
                            isLoading = false,
                            tasks = tasks
                        )
                    }
                    .onFailure { error ->
                        _uiState.value = TaskListUiState(
                            isLoading = false,
                            errorMessage = error.message
                                ?: "No se pudieron cargar las tareas."
                        )
                    }
            }
        }
    }

    fun toggleTask(task: Task) {

        val updatedTask = task.copy(
            completed = !task.completed,
            updatedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {

            updateTaskUseCase(updatedTask)
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = error.message
                            ?: "No se pudo actualizar la tarea."
                    )
                }
        }
    }

    fun deleteTask(task: Task) {

        viewModelScope.launch {

            deleteTaskUseCase(task.id)
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = error.message
                            ?: "No se pudo eliminar la tarea."
                    )
                }
        }
    }
}