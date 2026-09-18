package com.sena.taskmanager.ui.screen.drafts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun DraftsScreen(
    onBack: () -> Unit,
    onEditDraft: (TaskDraft) -> Unit,
    viewModel: DraftsViewModel = hiltViewModel()
) {

    val drafts by viewModel.drafts.collectAsStateWithLifecycle()
    val operationState by viewModel.operationState
        .collectAsStateWithLifecycle()

    var draftToDelete by remember {
        mutableStateOf<TaskDraft?>(null)
    }

    var draftToPublish by remember {
        mutableStateOf<TaskDraft?>(null)
    }

    LaunchedEffect(Unit) {
        viewModel.loadDrafts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Borradores"
            )

            TextButton(
                onClick = onBack
            ) {
                Text("Volver")
            }
        }

        when {

            operationState is OperationState.Loading -> {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {

                    CircularProgressIndicator()
                }
            }

            operationState is OperationState.Error -> {

                Text(
                    text = (operationState as OperationState.Error).message,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }

        if (drafts.isEmpty()) {

            Text(
                text = "No tienes borradores.",
                modifier = Modifier.padding(top = 24.dp)
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(
                    items = drafts,
                    key = { it.id }
                ) { draft ->

                    DraftItem(
                        draft = draft,
                        onEdit = {
                            onEditDraft(draft)
                        },
                        onPublish = {
                            draftToPublish = draft
                        },
                        onDelete = {
                            draftToDelete = draft
                        }
                    )
                }
            }
        }
    }

    // Confirmación de eliminar
    draftToDelete?.let { draft ->

        AlertDialog(
            onDismissRequest = {
                draftToDelete = null
            },

            title = {
                Text("Eliminar borrador")
            },

            text = {
                Text(
                    "¿Seguro que deseas eliminar \"${draft.title}\"?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        viewModel.deleteDraft(draft)

                        draftToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        draftToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Confirmación de publicar
    draftToPublish?.let { draft ->

        AlertDialog(
            onDismissRequest = {
                draftToPublish = null
            },

            title = {
                Text("Publicar tarea")
            },

            text = {
                Text(
                    "¿Deseas publicar \"${draft.title}\" como una tarea?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        viewModel.publishDraft(draft)

                        draftToPublish = null
                    }
                ) {
                    Text("Publicar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        draftToPublish = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun DraftItem(
    draft: TaskDraft,
    onEdit: () -> Unit,
    onPublish: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = draft.title
            )

            if (draft.description.isNotBlank()) {

                Text(
                    text = draft.description,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(
                    onClick = onEdit
                ) {
                    Text("Editar")
                }

                TextButton(
                    onClick = onPublish
                ) {
                    Text("Publicar")
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
