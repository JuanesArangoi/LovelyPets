package com.example.demoapp.features.reports

import androidx.lifecycle.ViewModel
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetStatus
import com.example.demoapp.domain.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para el dashboard de reportes del administrador.
 * Calcula las estadísticas globales de publicaciones y las más recientes.
 */
@HiltViewModel
class AdminReportsViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    /** Total de publicaciones en la plataforma. */
    fun getTotalCount(): Int = petRepository.pets.value.size

    /** Publicaciones activas (verificadas y no resueltas). */
    fun getActiveCount(): Int =
        petRepository.pets.value.count { it.status == PetStatus.VERIFICADO }

    /** Publicaciones resueltas. */
    fun getResolvedCount(): Int =
        petRepository.pets.value.count { it.status == PetStatus.RESUELTO }

    /** Publicaciones sin resolver (pendientes + verificadas = no resueltas). */
    fun getUnresolvedCount(): Int =
        petRepository.pets.value.count {
            it.status == PetStatus.PENDIENTE || it.status == PetStatus.VERIFICADO
        }

    /** Publicaciones pendientes de moderación. */
    fun getPendingCount(): Int =
        petRepository.pets.value.count { it.status == PetStatus.PENDIENTE }

    /** Publicaciones rechazadas. */
    fun getRejectedCount(): Int =
        petRepository.pets.value.count { it.status == PetStatus.RECHAZADO }

    /** Últimas 10 publicaciones ordenadas por fecha de creación descendente. */
    fun getRecentPets(): List<Pet> =
        petRepository.pets.value
            .sortedByDescending { it.createdAt }
            .take(10)
}
