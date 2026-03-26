package com.example.demoapp.data.repository

import com.example.demoapp.domain.model.Comment
import com.example.demoapp.domain.model.Location
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory
import com.example.demoapp.domain.model.PetStatus
import com.example.demoapp.domain.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio de publicaciones de mascotas.
 * Gestiona CRUD, filtrado, votación y moderación de publicaciones en memoria.
 * Anotado con @Singleton para que Hilt gestione una única instancia.
 */
@Singleton
class PetRepositoryImpl @Inject constructor() : PetRepository {

    // Lista reactiva de publicaciones
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    override val pets: StateFlow<List<Pet>> = _pets.asStateFlow()

    init {
        _pets.value = loadSampleData()
    }

    override fun getVerifiedPets(): List<Pet> {
        return _pets.value.filter { it.status == PetStatus.VERIFICADO }
    }

    override fun getPendingPets(): List<Pet> {
        return _pets.value.filter { it.status == PetStatus.PENDIENTE }
    }

    override fun getByCategory(category: PetCategory): List<Pet> {
        return _pets.value.filter { it.status == PetStatus.VERIFICADO && it.category == category }
    }

    override fun getByOwner(ownerId: String): List<Pet> {
        return _pets.value.filter { it.ownerId == ownerId }
    }

    override fun findById(id: String): Pet? {
        return _pets.value.firstOrNull { it.id == id }
    }

    override fun create(pet: Pet) {
        val newPet = pet.copy(id = UUID.randomUUID().toString())
        _pets.value = _pets.value + newPet
    }

    override fun update(pet: Pet): Boolean {
        val index = _pets.value.indexOfFirst { it.id == pet.id }
        if (index != -1) {
            val mutableList = _pets.value.toMutableList()
            mutableList[index] = pet
            _pets.value = mutableList
            return true
        }
        return false
    }

    override fun delete(id: String): Boolean {
        val sizeBefore = _pets.value.size
        _pets.value = _pets.value.filter { it.id != id }
        return _pets.value.size < sizeBefore
    }

    override fun vote(petId: String, userId: String): Boolean {
        val index = _pets.value.indexOfFirst { it.id == petId }
        if (index != -1) {
            val mutableList = _pets.value.toMutableList()
            mutableList[index] = mutableList[index].copy(votes = mutableList[index].votes + 1)
            _pets.value = mutableList
            return true
        }
        return false
    }

    override fun verify(petId: String): Boolean {
        val index = _pets.value.indexOfFirst { it.id == petId }
        if (index != -1) {
            val mutableList = _pets.value.toMutableList()
            mutableList[index] = mutableList[index].copy(status = PetStatus.VERIFICADO)
            _pets.value = mutableList
            return true
        }
        return false
    }

    override fun reject(petId: String, reason: String): Boolean {
        val index = _pets.value.indexOfFirst { it.id == petId }
        if (index != -1) {
            val mutableList = _pets.value.toMutableList()
            mutableList[index] = mutableList[index].copy(
                status = PetStatus.RECHAZADO,
                rejectionReason = reason
            )
            _pets.value = mutableList
            return true
        }
        return false
    }

    override fun resolve(petId: String): Boolean {
        val index = _pets.value.indexOfFirst { it.id == petId }
        if (index != -1) {
            val mutableList = _pets.value.toMutableList()
            mutableList[index] = mutableList[index].copy(status = PetStatus.RESUELTO)
            _pets.value = mutableList
            return true
        }
        return false
    }

    /**
     * Carga datos de ejemplo para demostración.
     */
    private fun loadSampleData(): List<Pet> {
        return listOf(
            Pet(
                id = "1",
                title = "Luna busca hogar 🐕",
                description = "Luna es una perrita mestiza de 2 años, muy cariñosa y juguetona. Está esterilizada y tiene todas sus vacunas al día.",
                category = PetCategory.ADOPCION,
                status = PetStatus.VERIFICADO,
                animalType = "Perro",
                breed = "Mestiza",
                size = "Mediano",
                hasVaccines = true,
                photoUrl = "https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=400",
                location = Location(4.8133, -75.6961),
                ownerId = "1",
                ownerName = "Juan Arango",
                votes = 12
            ),
            Pet(
                id = "2",
                title = "Gatito encontrado en el centro",
                description = "Se encontró un gatito naranja en el parque central. Parece tener unos 3 meses.",
                category = PetCategory.ENCONTRADOS,
                status = PetStatus.VERIFICADO,
                animalType = "Gato",
                breed = "Criollo",
                size = "Pequeño",
                hasVaccines = false,
                photoUrl = "https://images.unsplash.com/photo-1574158622682-e40e69881006?w=400",
                location = Location(4.8087, -75.6906),
                ownerId = "2",
                ownerName = "María López",
                votes = 8
            ),
            Pet(
                id = "3",
                title = "Se perdió Max 😢",
                description = "Max es un labrador dorado de 5 años. Se perdió en el barrio Cuba. Tiene collar azul con placa.",
                category = PetCategory.PERDIDOS,
                status = PetStatus.VERIFICADO,
                animalType = "Perro",
                breed = "Labrador",
                size = "Grande",
                hasVaccines = true,
                photoUrl = "https://images.unsplash.com/photo-1552053831-71594a27632d?w=400",
                location = Location(4.8050, -75.7100),
                ownerId = "1",
                ownerName = "Juan Arango",
                votes = 25
            ),
            Pet(
                id = "4",
                title = "Hogar temporal para conejo",
                description = "Necesitamos un hogar de paso temporal para un conejo durante 2 semanas.",
                category = PetCategory.TEMPORAL,
                status = PetStatus.VERIFICADO,
                animalType = "Conejo",
                breed = "Holland Lop",
                size = "Pequeño",
                hasVaccines = true,
                photoUrl = "https://images.unsplash.com/photo-1585110396000-c9ffd4e4b308?w=400",
                location = Location(4.8200, -75.6800),
                ownerId = "2",
                ownerName = "María López",
                votes = 5
            ),
            Pet(
                id = "5",
                title = "Jornada de vacunación gratuita 🏥",
                description = "Este sábado habrá jornada de vacunación gratuita para perros y gatos en el parque.",
                category = PetCategory.VETERINARIA,
                status = PetStatus.VERIFICADO,
                animalType = "Varios",
                breed = "N/A",
                size = "N/A",
                hasVaccines = false,
                photoUrl = "https://images.unsplash.com/photo-1548767797-d8c844163c4c?w=400",
                location = Location(4.8150, -75.6950),
                ownerId = "1",
                ownerName = "Juan Arango",
                votes = 30
            ),
            Pet(
                id = "6",
                title = "Adopta a Michi 🐱",
                description = "Michi es un gato siamés de 1 año. Es muy tranquilo y cariñoso.",
                category = PetCategory.ADOPCION,
                status = PetStatus.PENDIENTE,
                animalType = "Gato",
                breed = "Siamés",
                size = "Mediano",
                hasVaccines = true,
                photoUrl = "https://images.unsplash.com/photo-1513245543132-31f507417b26?w=400",
                location = Location(4.8300, -75.7000),
                ownerId = "2",
                ownerName = "María López",
                votes = 0
            )
        )
    }
}
