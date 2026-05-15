package com.example.demoapp.domain.repository

import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory
import kotlinx.coroutines.flow.StateFlow

/**
 * Interfaz del repositorio de publicaciones de mascotas.
 * Todas las operaciones son suspend para soportar Firestore asíncrono.
 */
interface PetRepository {
    val pets: StateFlow<List<Pet>>

    suspend fun getVerifiedPets(): List<Pet>
    suspend fun getPendingPets(): List<Pet>
    suspend fun getByCategory(category: PetCategory): List<Pet>
    suspend fun getByOwner(ownerId: String): List<Pet>
    suspend fun findById(id: String): Pet?
    suspend fun create(pet: Pet)
    suspend fun update(pet: Pet): Boolean
    suspend fun delete(id: String): Boolean
    suspend fun vote(petId: String, userId: String): Boolean
    suspend fun verify(petId: String): Boolean
    suspend fun reject(petId: String, reason: String): Boolean
    suspend fun resolve(petId: String): Boolean
    suspend fun incrementViewCount(petId: String)
}
