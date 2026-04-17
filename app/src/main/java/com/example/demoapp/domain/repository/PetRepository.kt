package com.example.demoapp.domain.repository

import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory
import kotlinx.coroutines.flow.StateFlow

/**
 * Interfaz del repositorio de publicaciones de mascotas.
 * Define las operaciones CRUD, filtrado, votación y moderación.
 */
interface PetRepository {
    val pets: StateFlow<List<Pet>>

    fun getVerifiedPets(): List<Pet>
    fun getPendingPets(): List<Pet>
    fun getByCategory(category: PetCategory): List<Pet>
    fun getByOwner(ownerId: String): List<Pet>
    fun findById(id: String): Pet?
    fun create(pet: Pet)
    fun update(pet: Pet): Boolean
    fun delete(id: String): Boolean
    fun vote(petId: String, userId: String): Boolean
    fun verify(petId: String): Boolean
    fun reject(petId: String, reason: String): Boolean
    fun resolve(petId: String): Boolean
    fun incrementViewCount(petId: String)
}
