package com.sena.taskmanager.ui.screen.drafts

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
import com.sena.taskmanager.domain.model.TaskDraft
import com.sena.taskmanager.ui.state.OperationState

@Composable
fun DraftFormScreen(
    draft: TaskDraft,
    onBack: () -> Unit,
    viewModel: DraftFormViewModel = hiltViewModel()
) {
    var title by remember(draft) {
        mutableStateOf(draft.title)
    }

    var description by remember(draft) {
        mutableStateOf(draft.description)
    }

    val operationState by viewModel.operationState
        .collectAsStateWithLifecycle()

    LaunchedEffect(operationState) {
        if (operationState is OperationState.Success) {
            onBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Editar Borrador")

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Título") },
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Descripción") },
            minLines = 4
        )

        if (operationState is OperationState.Error) {
            Text(text = (operationState as OperationState.Error).message)
        }

        if (operationState is OperationState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    viewModel.updateDraft(
                        id = draft.id,
                        title = title,
                        description = description
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
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
