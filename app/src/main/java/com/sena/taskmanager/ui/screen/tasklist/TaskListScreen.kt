package com.sena.taskmanager.ui.screen.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.sena.taskmanager.domain.model.Task

@Composable
fun TaskListScreen(
    onCreateTask: () -> Unit,
    onEditTask: (Task) -> Unit,
    onDrafts: () -> Unit,
    onLogout: () -> Unit,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var taskToDelete by remember {
        mutableStateOf<Task?>(null)
    }

    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Mis tareas"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = onCreateTask,
                modifier = Modifier.weight(1f)
            ) {
                Text("Nueva tarea")
            }

            OutlinedButton(
                onClick = onDrafts,
                modifier = Modifier.weight(1f)
            ) {
                Text("Borradores")
            }
        }

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salir")
        }

        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage ?: "Error"
                )
            }

            uiState.tasks.isEmpty() -> {
                Text(
                    text = "No tienes tareas todavía.",
                    modifier = Modifier.padding(top = 24.dp)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        items = uiState.tasks,
                        key = { it.id }
                    ) { task ->

                        TaskItem(
                            task = task,
                            onToggle = {
                                viewModel.toggleTask(task)
                            },
                            onEdit = {
                                onEditTask(task)
                            },
                            onDelete = {
                                taskToDelete = task
                            }
                        )
                    }
                }
            }
        }
    }

    taskToDelete?.let { task ->

        AlertDialog(
            onDismissRequest = {
                taskToDelete = null
            },
            title = {
                Text("Eliminar tarea")
            },
            text = {
                Text(
                    "¿Seguro que deseas eliminar \"${task.title}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteTask(task)
                        taskToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        taskToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = task.completed,
                    onCheckedChange = {
                        onToggle()
                    }
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.title
                    )

                    if (task.description.isNotBlank()) {
                        Text(
                            text = task.description,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Text(
                        text = if (task.completed) {
                            "Completada"
                        } else {
                            "Pendiente"
                        },
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(
                    onClick = onEdit
                ) {
                    Text("Editar")
                }

                TextButton(
                    onClick = onDelete
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}