package com.sena.taskmanager.domain.repository

interface AuthRepository {

    suspend fun register(
        email: String,
        password: String
    ): Result<String>

    suspend fun login(
        email: String,
        password: String
    ): Result<String>

    fun getCurrentUserId(): String?

    fun logout()
}