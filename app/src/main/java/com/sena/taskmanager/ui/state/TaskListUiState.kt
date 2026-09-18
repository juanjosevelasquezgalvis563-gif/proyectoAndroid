package com.sena.taskmanager.ui.state


import com.sena.taskmanager.domain.model.Task

data class TaskListUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val errorMessage: String? = null
)