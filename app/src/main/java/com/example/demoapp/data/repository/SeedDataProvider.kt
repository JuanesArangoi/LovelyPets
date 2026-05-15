package com.example.demoapp.data.repository

import com.example.demoapp.domain.model.Comment
import com.example.demoapp.domain.model.Location
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory
import com.example.demoapp.domain.model.PetStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Utilidad para sembrar datos de ejemplo en Firestore.
 * Se ejecuta solo si la colección "pets" está vacía.
 */
object SeedDataProvider {

    suspend fun seedIfEmpty(firestore: FirebaseFirestore) {
        val petsSnapshot = firestore.collection("pets").get().await()
        if (petsSnapshot.isEmpty) {
            seedPets(firestore)
        }

        val commentsSnapshot = firestore.collection("comments").get().await()
        if (commentsSnapshot.isEmpty) {
            seedComments(firestore)
        }
    }

    private suspend fun seedPets(firestore: FirebaseFirestore) {
        val samplePets = listOf(
            Pet(
                title = "Luna busca hogar",
                description = "Luna es una gatita de 2 años muy cariñosa y juguetona. Está esterilizada y con todas sus vacunas al día.",
                category = PetCategory.ADOPCION,
                status = PetStatus.VERIFICADO,
                animalType = "Gato",
                breed = "Mestiza",
                size = "Pequeño",
                hasVaccines = true,
                photoUrl = "https://images.unsplash.com/photo-1574158622682-e40e69881006?w=400",
                location = Location(4.8133, -75.6961),
                ownerId = "seed_user_1",
                ownerName = "Juan Arango"
            ),
            Pet(
                title = "Toby necesita familia",
                description = "Toby es un perro golden retriever de 3 años, muy sociable y entrenado. Ideal para familias con niños.",
                category = PetCategory.ADOPCION,
                status = PetStatus.VERIFICADO,
                animalType = "Perro",
                breed = "Golden Retriever",
                size = "Grande",
                hasVaccines = true,
                photoUrl = "https://images.unsplash.com/photo-1552053831-71594a27632d?w=400",
                location = Location(4.8100, -75.6900),
                ownerId = "seed_user_1",
                ownerName = "Juan Arango"
            ),
            Pet(
                title = "Perrito perdido en centro",
                description = "Se perdió un perro pequeño color café con collar rojo en el centro de la ciudad. Responde al nombre de Max.",
                category = PetCategory.PERDIDOS,
                status = PetStatus.VERIFICADO,
                animalType = "Perro",
                breed = "Mestizo",
                size = "Pequeño",
                hasVaccines = false,
                photoUrl = "https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=400",
                location = Location(4.8150, -75.6980),
                ownerId = "seed_user_2",
                ownerName = "María López"
            ),
            Pet(
                title = "Gatito encontrado",
                description = "Encontramos un gatito naranja en el parque del barrio. Parece tener dueño, tiene collar azul pero sin placa.",
                category = PetCategory.ENCONTRADOS,
                status = PetStatus.VERIFICADO,
                animalType = "Gato",
                size = "Pequeño",
                hasVaccines = false,
                photoUrl = "https://images.unsplash.com/photo-1526336024174-e58f5cdd8e13?w=400",
                location = Location(4.8200, -75.7000),
                ownerId = "seed_user_2",
                ownerName = "María López"
            ),
            Pet(
                title = "Hogar temporal para cachorros",
                description = "Buscamos hogar temporal para 3 cachorros rescatados de la calle. Son juguetones y sanos, ya están desparasitados.",
                category = PetCategory.TEMPORAL,
                status = PetStatus.VERIFICADO,
                animalType = "Perro",
                size = "Pequeño",
                hasVaccines = true,
                photoUrl = "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=400",
                location = Location(4.8050, -75.6850),
                ownerId = "seed_user_1",
                ownerName = "Juan Arango"
            ),
            Pet(
                title = "Jornada de vacunación gratuita",
                description = "Este sábado habrá jornada de vacunación gratuita para perros y gatos en la plaza principal. Trae a tu mascota con correa o guacal.",
                category = PetCategory.VETERINARIA,
                status = PetStatus.PENDIENTE,
                animalType = "Todos",
                size = "Todos",
                hasVaccines = false,
                photoUrl = "https://images.unsplash.com/photo-1612531386530-97d561db8b9b?w=400",
                location = Location(4.8180, -75.6950),
                ownerId = "seed_user_2",
                ownerName = "María López"
            )
        )

        samplePets.forEach { pet ->
            val docRef = firestore.collection("pets").add(pet).await()
            firestore.collection("pets").document(docRef.id).update("id", docRef.id).await()
        }
    }

    private suspend fun seedComments(firestore: FirebaseFirestore) {
        val sampleComments = listOf(
            Comment(
                petId = "seed",
                authorId = "seed_user_2",
                authorName = "María López",
                text = "¡Qué hermosa! Me gustaría saber más sobre ella."
            ),
            Comment(
                petId = "seed",
                authorId = "seed_user_2",
                authorName = "María López",
                text = "Vi un perro similar cerca de la universidad, revisaré mañana."
            ),
            Comment(
                petId = "seed",
                authorId = "seed_user_1",
                authorName = "Juan Arango",
                text = "¡Gracias! Cualquier información es valiosa."
            )
        )

        sampleComments.forEach { comment ->
            firestore.collection("comments").add(comment).await()
        }
    }
}
