package com.sena.taskmanager.ui.navHost

import androidx.lifecycle.ViewModel
import com.sena.taskmanager.domain.usecase.auth.GetCurrentUserUseCase
import com.sena.taskmanager.domain.usecase.auth.LogoutUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
) : ViewModel() {

    fun isUserLoggedIn(): Boolean {
        return getCurrentUserUseCase() != null
    }

    fun logout() {
        logoutUserUseCase()
    }
}