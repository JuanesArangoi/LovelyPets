package com.example.demoapp.data.repository

import com.example.demoapp.domain.model.User
import com.example.demoapp.domain.model.UserRole
import com.example.demoapp.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio de usuarios con Firebase Firestore y Auth.
 * Gestiona registro (Auth + Firestore), login (Auth), y CRUD en Firestore.
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserRepository {

    private val collection = firestore.collection("users")

    private val _users = MutableStateFlow<List<User>>(emptyList())
    override val users: StateFlow<List<User>> = _users.asStateFlow()

    init {
        // Escuchar cambios en tiempo real en la colección de usuarios
        collection.addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                _users.value = it.documents.mapNotNull { snap ->
                    snap.toObject(User::class.java)?.apply { id = snap.id }
                }
            }
        }
    }

    override suspend fun save(user: User, password: String) {
        // Crear usuario en Firebase Authentication
        val newUser = auth.createUserWithEmailAndPassword(user.email, password).await()
        val uid = newUser.user?.uid ?: throw Exception("Error al obtener el UID del usuario creado")

        // Guardar datos del usuario en Firestore (sin la contraseña)
        val userCopy = user.copy(id = uid)
        collection.document(uid).set(userCopy).await()
    }

    override suspend fun login(email: String, password: String): User? {
        // Autenticar con Firebase Auth
        val responseUser = auth.signInWithEmailAndPassword(email, password).await()
        val uid = responseUser.user?.uid ?: throw Exception("Usuario no encontrado")
        // Recuperar datos desde Firestore
        return findById(uid)
    }

    override suspend fun findById(id: String): User? {
        val snapshot = collection.document(id).get().await()
        return snapshot.toObject(User::class.java)?.apply { this.id = snapshot.id }
    }

    override suspend fun updateUser(user: User): Boolean {
        return try {
            collection.document(user.id).set(user).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteUser(id: String): Boolean {
        return try {
            collection.document(id).delete().await()
            // También eliminar de Firebase Auth si es el usuario actual
            auth.currentUser?.delete()?.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getAll(): List<User> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(User::class.java)?.apply { id = it.id }
        }
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
