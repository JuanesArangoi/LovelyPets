package com.example.demoapp.data.repository

import com.example.demoapp.domain.model.Achievement
import com.example.demoapp.domain.model.PetCategory
import com.example.demoapp.domain.model.PetStatus
import com.example.demoapp.domain.repository.AchievementRepository
import com.example.demoapp.domain.repository.PetRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio de logros.
 * Calcula dinámicamente el estado de cada logro consultando las publicaciones del usuario.
 */
@Singleton
class AchievementRepositoryImpl @Inject constructor(
    private val petRepository: PetRepository
) : AchievementRepository {

    override fun getAchievementsForUser(userId: String): List<Achievement> {
        val userPets = petRepository.getByOwner(userId)

        val totalPublications = userPets.size
        val resolvedCount = userPets.count { it.status == PetStatus.RESUELTO }
        val verifiedCount = userPets.count { it.status == PetStatus.VERIFICADO }
        val adoptionCount = userPets.count { it.category == PetCategory.ADOPCION }
        val lostCount = userPets.count { it.category == PetCategory.PERDIDOS }
        val foundCount = userPets.count { it.category == PetCategory.ENCONTRADOS }
        val temporalCount = userPets.count { it.category == PetCategory.TEMPORAL }
        val totalVotes = userPets.sumOf { it.votes }

        return listOf(
            Achievement(
                id = "first_publication",
                title = "Mi primera publicación",
                description = "Crea tu primera publicación en LovelyPets",
                icon = "📝",
                isUnlocked = totalPublications >= 1,
                progress = totalPublications.coerceAtMost(1),
                target = 1
            ),
            Achievement(
                id = "first_resolved",
                title = "Primer caso resuelto",
                description = "Resuelve tu primer caso de mascota",
                icon = "✅",
                isUnlocked = resolvedCount >= 1,
                progress = resolvedCount.coerceAtMost(1),
                target = 1
            ),
            Achievement(
                id = "first_adoption",
                title = "Corazón adoptivo",
                description = "Publica tu primera mascota en adopción",
                icon = "🏠",
                isUnlocked = adoptionCount >= 1,
                progress = adoptionCount.coerceAtMost(1),
                target = 1
            ),
            Achievement(
                id = "rescuer",
                title = "Rescatista",
                description = "Reporta una mascota encontrada",
                icon = "🦸",
                isUnlocked = foundCount >= 1,
                progress = foundCount.coerceAtMost(1),
                target = 1
            ),
            Achievement(
                id = "lost_helper",
                title = "Buscador incansable",
                description = "Publica una mascota perdida para ayudar a encontrarla",
                icon = "🔍",
                isUnlocked = lostCount >= 1,
                progress = lostCount.coerceAtMost(1),
                target = 1
            ),
            Achievement(
                id = "temporary_home",
                title = "Hogar de paso",
                description = "Ofrece un hogar temporal para una mascota",
                icon = "🏡",
                isUnlocked = temporalCount >= 1,
                progress = temporalCount.coerceAtMost(1),
                target = 1
            ),
            Achievement(
                id = "five_publications",
                title = "Publicador activo",
                description = "Crea 5 publicaciones en la plataforma",
                icon = "🌟",
                isUnlocked = totalPublications >= 5,
                progress = totalPublications.coerceAtMost(5),
                target = 5
            ),
            Achievement(
                id = "three_resolved",
                title = "Solucionador",
                description = "Resuelve 3 casos de mascotas",
                icon = "🏆",
                isUnlocked = resolvedCount >= 3,
                progress = resolvedCount.coerceAtMost(3),
                target = 3
            ),
            Achievement(
                id = "community_favorite",
                title = "Favorito de la comunidad",
                description = "Acumula 10 votos en tus publicaciones",
                icon = "❤️",
                isUnlocked = totalVotes >= 10,
                progress = totalVotes.coerceAtMost(10),
                target = 10
            ),
            Achievement(
                id = "verified_publisher",
                title = "Publicador verificado",
                description = "Ten 3 publicaciones verificadas por moderadores",
                icon = "🛡️",
                isUnlocked = verifiedCount >= 3,
                progress = verifiedCount.coerceAtMost(3),
                target = 3
            )
        )
    }
}
