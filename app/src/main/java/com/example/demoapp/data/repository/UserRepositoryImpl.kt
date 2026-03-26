package com.example.demoapp.data.repository

import com.example.demoapp.domain.model.User
import com.example.demoapp.domain.model.UserRole
import com.example.demoapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio de usuarios.
 * Gestiona el login, registro, sesión actual y CRUD de usuarios en memoria.
 * Reemplaza al antiguo SessionManager con inyección de dependencias via Hilt.
 */
@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {

    // Lista reactiva de usuarios registrados
    private val _users = MutableStateFlow<List<User>>(emptyList())
    override val users: StateFlow<List<User>> = _users.asStateFlow()

    // Usuario actualmente logueado
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        _users.value = loadSampleUsers()
    }

    override fun login(email: String, password: String): User? {
        val user = _users.value.firstOrNull { it.email == email && it.password == password }
        _currentUser.value = user
        return user
    }

    override fun register(user: User): Boolean {
        if (_users.value.any { it.email == user.email }) {
            return false // El email ya está registrado
        }
        _users.value = _users.value + user
        _currentUser.value = user
        return true
    }

    override fun logout() {
        _currentUser.value = null
    }

    override fun findById(id: String): User? {
        return _users.value.firstOrNull { it.id == id }
    }

    override fun updateUser(user: User): Boolean {
        val index = _users.value.indexOfFirst { it.id == user.id }
        if (index != -1) {
            val mutableList = _users.value.toMutableList()
            mutableList[index] = user
            _users.value = mutableList
            if (_currentUser.value?.id == user.id) {
                _currentUser.value = user
            }
            return true
        }
        return false
    }

    override fun deleteUser(id: String): Boolean {
        val user = _users.value.firstOrNull { it.id == id } ?: return false
        _users.value = _users.value.filter { it.id != id }
        if (_currentUser.value?.id == id) {
            _currentUser.value = null
        }
        return true
    }

    /**
     * Carga usuarios de ejemplo para la fase 2 (datos en memoria).
     */
    private fun loadSampleUsers(): List<User> {
        return listOf(
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
    }
}
