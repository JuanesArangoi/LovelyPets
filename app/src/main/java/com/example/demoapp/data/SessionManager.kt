package com.example.demoapp.data

import com.example.demoapp.domain.model.User
import com.example.demoapp.domain.model.UserRole

/**
 * Singleton que maneja la sesión del usuario actual.
 * Almacena el usuario logueado en memoria para que sea accesible
 * desde cualquier ViewModel de la aplicación.
 */
object SessionManager {

    // Usuario actualmente logueado (null si no hay sesión activa)
    var currentUser: User? = null
        private set

    // Lista de usuarios registrados en memoria (datos de ejemplo)
    private val registeredUsers = mutableListOf(
        User(
            id = "1",
            name = "Juan Arango",
            email = "juan@email.com",
            password = "123456",
            phoneNumber = "3001234567",
            city = "Pereira",
            address = "Calle 123",
            profilePictureUrl = "https://picsum.photos/200?random=1"
        ),
        User(
            id = "2",
            name = "María López",
            email = "maria@email.com",
            password = "123456",
            phoneNumber = "3009876543",
            city = "Armenia",
            address = "Carrera 456",
            profilePictureUrl = "https://picsum.photos/200?random=2"
        ),
        User(
            id = "3",
            name = "Admin Moderador",
            email = "admin@email.com",
            password = "admin123",
            phoneNumber = "3005555555",
            city = "Manizales",
            address = "Avenida Principal",
            role = UserRole.MODERADOR,
            profilePictureUrl = "https://picsum.photos/200?random=3"
        )
    )

    /**
     * Intenta iniciar sesión con email y contraseña.
     * Retorna true si las credenciales son válidas, false en caso contrario.
     */
    fun login(email: String, password: String): Boolean {
        val user = registeredUsers.find { it.email == email && it.password == password }
        currentUser = user
        return user != null
    }

    /**
     * Registra un nuevo usuario en la lista de usuarios.
     * Retorna true si el registro fue exitoso, false si el email ya existe.
     */
    fun register(user: User): Boolean {
        if (registeredUsers.any { it.email == user.email }) {
            return false // El email ya está registrado
        }
        registeredUsers.add(user)
        currentUser = user
        return true
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    fun logout() {
        currentUser = null
    }

    /**
     * Actualiza los datos del usuario actual.
     */
    fun updateCurrentUser(updatedUser: User) {
        val index = registeredUsers.indexOfFirst { it.id == updatedUser.id }
        if (index != -1) {
            registeredUsers[index] = updatedUser
            currentUser = updatedUser
        }
    }

    /**
     * Elimina la cuenta del usuario actual.
     */
    fun deleteCurrentUser(): Boolean {
        val user = currentUser ?: return false
        registeredUsers.removeAll { it.id == user.id }
        currentUser = null
        return true
    }

    /**
     * Obtiene un usuario por su ID.
     */
    fun findUserById(id: String): User? {
        return registeredUsers.find { it.id == id }
    }
}
