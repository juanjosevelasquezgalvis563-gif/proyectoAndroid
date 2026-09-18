package com.sena.taskmanager.domain.usecase.auth

import com.sena.taskmanager.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() {
        repository.logout()
    }
}

