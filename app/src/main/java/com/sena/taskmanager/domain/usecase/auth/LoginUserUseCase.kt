package com.sena.taskmanager.domain.usecase.auth

import com.sena.taskmanager.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<String> {
        return repository.login(email, password)
    }
}