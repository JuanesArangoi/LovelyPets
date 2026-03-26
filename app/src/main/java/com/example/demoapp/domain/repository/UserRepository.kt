package com.example.demoapp.domain.repository

import com.example.demoapp.domain.model.User
import kotlinx.coroutines.flow.StateFlow

/**
 * Interfaz del repositorio de usuarios.
 * Define las operaciones para gestionar los datos de usuarios
 * sin preocuparse por los detalles de implementación.
 */
interface UserRepository {
    val users: StateFlow<List<User>>
    val currentUser: StateFlow<User?>

    fun login(email: String, password: String): User?
    fun register(user: User): Boolean
    fun logout()
    fun findById(id: String): User?
    fun updateUser(user: User): Boolean
    fun deleteUser(id: String): Boolean
}
