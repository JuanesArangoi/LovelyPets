package com.example.demoapp.data.repository

import com.example.demoapp.domain.model.Comment
import com.example.demoapp.domain.model.Location
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory
import com.example.demoapp.domain.model.PetStatus
import java.util.UUID

/**
 * Repositorio singleton que gestiona las publicaciones de mascotas en memoria.
 * Proporciona operaciones CRUD y funciones de filtrado para las publicaciones.
 */
object PetRepository {

    // Lista mutable de mascotas en memoria
    private val pets = mutableListOf<Pet>()

    // Inicializar con datos de ejemplo
    init {
        loadSampleData()
    }

    /**
     * Obtiene todas las publicaciones verificadas (visibles en el feed público).
     */
    fun getVerifiedPets(): List<Pet> {
        return pets.filter { it.status == PetStatus.VERIFICADO }
    }

    /**
     * Obtiene todas las publicaciones (para moderadores).
     */
    fun getAllPets(): List<Pet> {
        return pets.toList()
    }

    /**
     * Obtiene publicaciones pendientes de verificación (para moderadores).
     */
    fun getPendingPets(): List<Pet> {
        return pets.filter { it.status == PetStatus.PENDIENTE }
    }

    /**
     * Obtiene las publicaciones de un usuario específico.
     */
    fun getPetsByOwner(ownerId: String): List<Pet> {
        return pets.filter { it.ownerId == ownerId }
    }

    /**
     * Filtra publicaciones verificadas por categoría.
     */
    fun getVerifiedPetsByCategory(category: PetCategory): List<Pet> {
        return pets.filter { it.status == PetStatus.VERIFICADO && it.category == category }
    }

    /**
     * Busca una publicación por su ID.
     */
    fun findById(id: String): Pet? {
        return pets.find { it.id == id }
    }

    /**
     * Crea una nueva publicación de mascota.
     * La publicación se crea con estado PENDIENTE por defecto.
     */
    fun createPet(pet: Pet): Pet {
        val newPet = pet.copy(id = UUID.randomUUID().toString())
        pets.add(newPet)
        return newPet
    }

    /**
     * Actualiza una publicación existente.
     */
    fun updatePet(updatedPet: Pet): Boolean {
        val index = pets.indexOfFirst { it.id == updatedPet.id }
        if (index != -1) {
            pets[index] = updatedPet
            return true
        }
        return false
    }

    /**
     * Elimina una publicación por su ID.
     */
    fun deletePet(id: String): Boolean {
        return pets.removeAll { it.id == id }
    }

    /**
     * Agrega un voto "Me interesa" a una publicación.
     */
    fun votePet(petId: String): Boolean {
        val index = pets.indexOfFirst { it.id == petId }
        if (index != -1) {
            pets[index] = pets[index].copy(votes = pets[index].votes + 1)
            return true
        }
        return false
    }

    /**
     * Verifica una publicación (acción de moderador).
     */
    fun verifyPet(petId: String): Boolean {
        val index = pets.indexOfFirst { it.id == petId }
        if (index != -1) {
            pets[index] = pets[index].copy(status = PetStatus.VERIFICADO)
            return true
        }
        return false
    }

    /**
     * Rechaza una publicación con un motivo (acción de moderador).
     */
    fun rejectPet(petId: String, reason: String): Boolean {
        val index = pets.indexOfFirst { it.id == petId }
        if (index != -1) {
            pets[index] = pets[index].copy(
                status = PetStatus.RECHAZADO,
                rejectionReason = reason
            )
            return true
        }
        return false
    }

    /**
     * Marca una publicación como resuelta/finalizada.
     */
    fun resolvePet(petId: String): Boolean {
        val index = pets.indexOfFirst { it.id == petId }
        if (index != -1) {
            pets[index] = pets[index].copy(status = PetStatus.RESUELTO)
            return true
        }
        return false
    }

    /**
     * Agrega un comentario a una publicación.
     */
    fun addComment(petId: String, comment: Comment): Boolean {
        val index = pets.indexOfFirst { it.id == petId }
        if (index != -1) {
            val updatedComments = pets[index].comments + comment
            pets[index] = pets[index].copy(comments = updatedComments)
            return true
        }
        return false
    }

    /**
     * Carga datos de ejemplo para demostración.
     */
    private fun loadSampleData() {
        pets.addAll(
            listOf(
                Pet(
                    id = "1",
                    title = "Luna busca hogar 🐕",
                    description = "Luna es una perrita mestiza de 2 años, muy cariñosa y juguetona. Está esterilizada y tiene todas sus vacunas al día. Ideal para familias con niños.",
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
                    votes = 12,
                    comments = listOf(
                        Comment(
                            id = "c1",
                            petId = "1",
                            authorId = "2",
                            authorName = "María López",
                            text = "¡Qué hermosa! Me gustaría saber más sobre ella."
                        )
                    )
                ),
                Pet(
                    id = "2",
                    title = "Gatito encontrado en el centro",
                    description = "Se encontró un gatito naranja en el parque central. Parece tener unos 3 meses. Está en buen estado de salud pero necesita un hogar.",
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
                    description = "Max es un labrador dorado de 5 años. Se perdió en el barrio Cuba. Tiene collar azul con placa. Recompensa para quien lo encuentre.",
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
                    votes = 25,
                    comments = listOf(
                        Comment(
                            id = "c2",
                            petId = "3",
                            authorId = "2",
                            authorName = "María López",
                            text = "Vi un perro similar cerca de la universidad, revisaré mañana."
                        ),
                        Comment(
                            id = "c3",
                            petId = "3",
                            authorId = "1",
                            authorName = "Juan Arango",
                            text = "¡Gracias! Cualquier información es valiosa."
                        )
                    )
                ),
                Pet(
                    id = "4",
                    title = "Hogar temporal para conejo",
                    description = "Necesitamos un hogar de paso temporal para un conejo durante 2 semanas mientras su dueño se recupera de cirugía.",
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
                    description = "Este sábado habrá jornada de vacunación gratuita para perros y gatos en el parque del barrio. Vacunas antirrábica y triple felina. Cupos limitados.",
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
                    description = "Michi es un gato siamés de 1 año. Es muy tranquilo y cariñoso, ideal para apartamentos.",
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
        )
    }
}
