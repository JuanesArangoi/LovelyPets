package com.example.demoapp.features.moderator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.data.repository.PetRepository
import com.example.demoapp.domain.model.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel que gestiona el panel de moderación.
 * Permite ver publicaciones pendientes y verificarlas o rechazarlas.
 */
class ModeratorViewModel : ViewModel() {

    // Lista de publicaciones pendientes de verificación
    private val _pendingPets = MutableStateFlow<List<Pet>>(emptyList())
    val pendingPets: StateFlow<List<Pet>> = _pendingPets.asStateFlow()

    // Motivo de rechazo (para el diálogo)
    var rejectionReason by mutableStateOf("")
        private set

    // Inicializar cargando publicaciones pendientes
    init {
        loadPendingPets()
    }

    /**
     * Carga las publicaciones pendientes de verificación.
     */
    fun loadPendingPets() {
        _pendingPets.value = PetRepository.getPendingPets()
    }

    /**
     * Verifica una publicación (la aprueba).
     */
    fun verifyPet(petId: String) {
        PetRepository.verifyPet(petId)
        loadPendingPets() // Recargar la lista
    }

    /**
     * Rechaza una publicación con un motivo.
     */
    fun rejectPet(petId: String) {
        if (rejectionReason.isNotBlank()) {
            PetRepository.rejectPet(petId, rejectionReason)
            rejectionReason = "" // Limpiar el motivo
            loadPendingPets() // Recargar la lista
        }
    }

    /**
     * Actualiza el texto del motivo de rechazo.
     */
    fun onRejectionReasonChange(reason: String) {
        rejectionReason = reason
    }

    /**
     * Limpia el motivo de rechazo.
     */
    fun clearRejectionReason() {
        rejectionReason = ""
    }
}
