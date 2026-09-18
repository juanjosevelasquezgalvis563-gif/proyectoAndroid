package com.sena.taskmanager.domain.usecase.auth

import com.sena.taskmanager.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): String? {
        return repository.getCurrentUserId()
    }
}