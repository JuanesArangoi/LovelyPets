package com.example.demoapp.data.repository

import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory
import com.example.demoapp.domain.model.PetStatus
import com.example.demoapp.domain.repository.PetRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio de mascotas con Firebase Firestore.
 * Incluye listener en tiempo real y operaciones CRUD asíncronas.
 */
@Singleton
class PetRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PetRepository {

    private val collection = firestore.collection("pets")

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    override val pets: StateFlow<List<Pet>> = _pets.asStateFlow()

    init {
        // Escuchar cambios en tiempo real
        collection.addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                _pets.value = it.documents.mapNotNull { snap ->
                    snap.toObject(Pet::class.java)?.apply { id = snap.id }
                }
            }
        }
    }

    override suspend fun getVerifiedPets(): List<Pet> {
        return _pets.value.filter { it.status == PetStatus.VERIFICADO }
    }

    override suspend fun getPendingPets(): List<Pet> {
        return _pets.value.filter { it.status == PetStatus.PENDIENTE }
    }

    override suspend fun getByCategory(category: PetCategory): List<Pet> {
        return _pets.value.filter { it.status == PetStatus.VERIFICADO && it.category == category }
    }

    override suspend fun getByOwner(ownerId: String): List<Pet> {
        return _pets.value.filter { it.ownerId == ownerId }
    }

    override suspend fun findById(id: String): Pet? {
        val snapshot = collection.document(id).get().await()
        return snapshot.toObject(Pet::class.java)?.apply { this.id = snapshot.id }
    }

    override suspend fun create(pet: Pet) {
        val docRef = collection.add(pet).await()
        // Actualizar el id en Firestore con el id del documento
        collection.document(docRef.id).update("id", docRef.id).await()
    }

    override suspend fun update(pet: Pet): Boolean {
        return try {
            collection.document(pet.id).set(pet).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun delete(id: String): Boolean {
        return try {
            collection.document(id).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun vote(petId: String, userId: String): Boolean {
        return try {
            val pet = findById(petId) ?: return false
            collection.document(petId).update("votes", pet.votes + 1).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun verify(petId: String): Boolean {
        return try {
            collection.document(petId).update("status", PetStatus.VERIFICADO.name).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun reject(petId: String, reason: String): Boolean {
        return try {
            collection.document(petId).update(
                mapOf("status" to PetStatus.RECHAZADO.name, "rejectionReason" to reason)
            ).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun resolve(petId: String): Boolean {
        return try {
            collection.document(petId).update("status", PetStatus.RESUELTO.name).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun incrementViewCount(petId: String) {
        try {
            val pet = findById(petId) ?: return
            collection.document(petId).update("viewCount", pet.viewCount + 1).await()
        } catch (_: Exception) { }
    }
}
