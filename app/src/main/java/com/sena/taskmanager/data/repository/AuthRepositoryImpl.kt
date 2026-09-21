package com.sena.taskmanager.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.sena.taskmanager.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun register(
        email: String,
        password: String
    ): Result<String> {
        return try {
            val result = auth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val user = result.user


            if (user != null) {
                Result.success(user.uid)
            } else {
                Result.failure(
                    Exception("No se pudo obtener el usuario.")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<String> {
        return try {
            val result = auth
                .signInWithEmailAndPassword(email, password)
                .await()

            val user = result.user

            if (user != null) {
                Result.success(user.uid)
            } else {
                Result.failure(
                    Exception("No se pudo obtener el usuario.")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override fun logout() {
        auth.signOut()
    }
}