package com.sena.taskmanager.ui.screen.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sena.taskmanager.domain.usecase.auth.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

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

    fun login() {

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

        if (currentState.password.isBlank()) {
            _uiState.value = currentState.copy(
                errorMessage = "La contraseña es obligatoria"
            )
            return
        }

        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {

            val result = loginUserUseCase(
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
                            ?: "No se pudo iniciar sesión"
                    )
                }
        }
    }
}