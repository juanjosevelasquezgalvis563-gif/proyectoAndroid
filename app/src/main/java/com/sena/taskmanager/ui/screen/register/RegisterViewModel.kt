package com.sena.taskmanager.ui.screen.register


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sena.taskmanager.domain.usecase.auth.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            errorMessage = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            errorMessage = null
        )
    }

    fun onConfirmPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = password,
            errorMessage = null
        )
    }

    fun register() {

        val currentState = _uiState.value

        if (currentState.isLoading) return

        if (currentState.email.isBlank()) {
            _uiState.value = currentState.copy(
                errorMessage = "El correo es obligatorio"
            )
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(currentState.email)
                .matches()
        ) {
            _uiState.value = currentState.copy(
                errorMessage = "Ingresa un correo válido"
            )
            return
        }

        if (currentState.password.length < 6) {
            _uiState.value = currentState.copy(
                errorMessage = "La contraseña debe tener mínimo 6 caracteres"
            )
            return
        }

        if (currentState.password != currentState.confirmPassword) {
            _uiState.value = currentState.copy(
                errorMessage = "Las contraseñas no coinciden"
            )
            return
        }

        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {

            val result = registerUserUseCase(
                currentState.email.trim(),
                currentState.password
            )

            result
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?: "No se pudo registrar el usuario"
                    )
                }
        }
    }
}