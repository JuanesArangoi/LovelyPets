package com.example.demoapp.domain.repository

import com.example.demoapp.domain.model.User
import kotlinx.coroutines.flow.StateFlow

/**
 * Interfaz del repositorio de usuarios.
 * Todas las operaciones son suspend para soportar Firestore asíncrono.
 */
interface UserRepository {
    val users: StateFlow<List<User>>

    suspend fun save(user: User, password: String)
    suspend fun login(email: String, password: String): User?
    suspend fun findById(id: String): User?
    suspend fun updateUser(user: User): Boolean
    suspend fun deleteUser(id: String): Boolean
    suspend fun getAll(): List<User>
    suspend fun sendPasswordResetEmail(email: String)
    fun signOut()
    fun getCurrentUserId(): String?
}
