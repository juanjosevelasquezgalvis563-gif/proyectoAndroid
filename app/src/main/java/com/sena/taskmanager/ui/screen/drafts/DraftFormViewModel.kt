package com.sena.taskmanager.ui.screen.drafts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sena.taskmanager.domain.model.TaskDraft
import com.sena.taskmanager.domain.usecase.auth.GetCurrentUserUseCase
import com.sena.taskmanager.domain.usecase.draft.UpdateDraftUseCase
import com.sena.taskmanager.ui.state.OperationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DraftFormViewModel @Inject constructor(
    private val updateDraftUseCase: UpdateDraftUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _operationState =
        MutableStateFlow<OperationState>(OperationState.Idle)

    val operationState: StateFlow<OperationState> =
        _operationState.asStateFlow()

    fun updateDraft(
        id: Int,
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

        val draft = TaskDraft(
            id = id,
            ownerId = userId,
            title = title.trim(),
            description = description.trim(),
            savedAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            _operationState.value = OperationState.Loading

            updateDraftUseCase(draft)
                .onSuccess {
                    _operationState.value = OperationState.Success
                }
                .onFailure { error ->
                    _operationState.value =
                        OperationState.Error(
                            error.message ?: "No se pudo actualizar el borrador."
                        )
                }
        }
    }
}
