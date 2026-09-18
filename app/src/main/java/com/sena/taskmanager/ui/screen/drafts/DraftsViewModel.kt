package com.sena.taskmanager.ui.screen.drafts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sena.taskmanager.domain.model.TaskDraft
import com.sena.taskmanager.domain.usecase.auth.GetCurrentUserUseCase
import com.sena.taskmanager.domain.usecase.draft.DeleteDraftUseCase
import com.sena.taskmanager.domain.usecase.draft.GetDraftsUseCase
import com.sena.taskmanager.domain.usecase.draft.PublishDraftUseCase
import com.sena.taskmanager.ui.state.OperationState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DraftsViewModel @Inject constructor(
    private val getDraftsUseCase: GetDraftsUseCase,
    private val deleteDraftUseCase: DeleteDraftUseCase,
    private val publishDraftUseCase: PublishDraftUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _drafts =
        MutableStateFlow<List<TaskDraft>>(emptyList())

    val drafts: StateFlow<List<TaskDraft>> =
        _drafts.asStateFlow()

    private val _operationState =
        MutableStateFlow<OperationState>(OperationState.Idle)

    val operationState: StateFlow<OperationState> =
        _operationState.asStateFlow()

    fun loadDrafts() {

        val userId = getCurrentUserUseCase() ?: return

        viewModelScope.launch {

            getDraftsUseCase(userId).collect {
                _drafts.value = it
            }
        }
    }

    fun deleteDraft(draft: TaskDraft) {

        viewModelScope.launch {

            _operationState.value = OperationState.Loading

            deleteDraftUseCase(draft)
                .onSuccess {
                    _operationState.value = OperationState.Success
                    loadDrafts()
                }
                .onFailure { error ->
                    _operationState.value =
                        OperationState.Error(
                            error.message
                                ?: "No se pudo eliminar el borrador."
                        )
                }
        }
    }

    fun publishDraft(draft: TaskDraft) {

        viewModelScope.launch {

            _operationState.value = OperationState.Loading

            publishDraftUseCase(draft)
                .onSuccess {
                    _operationState.value = OperationState.Success
                    loadDrafts()
                }
                .onFailure { error ->
                    _operationState.value =
                        OperationState.Error(
                            error.message
                                ?: "No se pudo publicar el borrador."
                        )
                }
        }
    }
}