package com.example.demoapp.features.moderator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para el panel de moderación.
 * Gestiona la verificación y rechazo de publicaciones pendientes.
 */
@HiltViewModel
class ModeratorViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    // Motivo de rechazo
    var rejectionReason by mutableStateOf("")
        private set

    /**
     * Obtiene las publicaciones pendientes de verificación.
     */
    fun getPendingPets(): List<Pet> {
        return petRepository.getPendingPets()
    }

    /**
     * Actualiza el motivo de rechazo.
     */
    fun onRejectionReasonChange(reason: String) {
        rejectionReason = reason
    }

    /**
     * Verifica una publicación (la hace visible en el feed).
     */
    fun verifyPet(petId: String) {
        petRepository.verify(petId)
    }

    /**
     * Rechaza una publicación con el motivo ingresado.
     */
    fun rejectPet(petId: String) {
        if (rejectionReason.isNotBlank()) {
            petRepository.reject(petId, rejectionReason)
            rejectionReason = ""
        }
    }
}
