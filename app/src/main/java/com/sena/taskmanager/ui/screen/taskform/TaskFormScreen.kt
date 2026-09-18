package com.sena.taskmanager.ui.screen.taskform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sena.taskmanager.domain.model.Task
import com.sena.taskmanager.ui.state.OperationState

@Composable
fun TaskFormScreen(
    task: Task?,
    onBack: () -> Unit,
    viewModel: TaskFormViewModel = hiltViewModel()
) {

    var title by remember(task) {
        mutableStateOf(task?.title ?: "")
    }

    var description by remember(task) {
        mutableStateOf(task?.description ?: "")
    }

    val operationState by viewModel.operationState
        .collectAsStateWithLifecycle()

    LaunchedEffect(operationState) {
        if (operationState is OperationState.Success) {
            onBack()
        }
    }

    val isEditing = task != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = if (isEditing) {
                "Editar tarea"
            } else {
                "Nueva tarea"
            }
        )

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Título")
            },
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Descripción")
            },
            minLines = 4
        )

        if (operationState is OperationState.Error) {
            Text(
                text = (operationState as OperationState.Error).message
            )
        }

        if (operationState is OperationState.Loading) {

            CircularProgressIndicator()

        } else {

            Button(
                onClick = {
                    viewModel.saveTask(
                        id = task?.id ?: "",
                        title = title,
                        description = description,
                        completed = task?.completed ?: false,
                        createdAt = task?.createdAt ?: 0L
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (isEditing) {
                        "Guardar cambios"
                    } else {
                        "Crear tarea"
                    }

                )


            }

            if (!isEditing) {

                OutlinedButton(
                    onClick = {
                        viewModel.saveDraft(
                            title = title,
                            description = description
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar borrador")
                }
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}