package com.example.demoapp.features.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.data.SessionManager
import com.example.demoapp.data.repository.PetRepository
import com.example.demoapp.domain.model.PetStatus
import com.example.demoapp.domain.model.User

/**
 * ViewModel que gestiona el perfil del usuario.
 * Muestra datos personales, estadísticas y permite editar el perfil.
 */
class UserProfileViewModel : ViewModel() {

    // Campos editables del perfil
    val name = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El nombre es obligatorio"
            value.trim().length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }
    }

    val phone = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El teléfono es obligatorio"
            value.length < 7 -> "El teléfono debe tener al menos 7 dígitos"
            else -> null
        }
    }

    val city = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "La ciudad es obligatoria"
            else -> null
        }
    }

    val address = ValidatedField("") { _ -> null }

    // Estadísticas del usuario
    var activePets by mutableStateOf(0)
        private set
    var resolvedPets by mutableStateOf(0)
        private set
    var pendingPets by mutableStateOf(0)
        private set

    // Resultado de la edición
    var editResult by mutableStateOf<Boolean?>(null)
        private set

    // Resultado de eliminación de cuenta
    var deleteResult by mutableStateOf<Boolean?>(null)
        private set

    /**
     * Carga los datos del usuario actual.
     */
    fun loadProfile() {
        val user = SessionManager.currentUser ?: return

        // Cargar datos en los campos editables
        name.onChange(user.name)
        phone.onChange(user.phoneNumber)
        city.onChange(user.city)
        address.onChange(user.address)

        // Calcular estadísticas
        val userPets = PetRepository.getPetsByOwner(user.id)
        activePets = userPets.count { it.status == PetStatus.VERIFICADO }
        resolvedPets = userPets.count { it.status == PetStatus.RESUELTO }
        pendingPets = userPets.count { it.status == PetStatus.PENDIENTE }
    }

    /**
     * Guarda los cambios del perfil.
     */
    fun saveProfile() {
        val currentUser = SessionManager.currentUser ?: return

        val updatedUser = currentUser.copy(
            name = name.value,
            phoneNumber = phone.value,
            city = city.value,
            address = address.value
        )

        SessionManager.updateCurrentUser(updatedUser)
        editResult = true
    }

    /**
     * Elimina la cuenta del usuario actual.
     */
    fun deleteAccount() {
        deleteResult = SessionManager.deleteCurrentUser()
    }

    /**
     * Resetea el resultado de edición.
     */
    fun resetEditResult() {
        editResult = null
    }

    /**
     * Resetea el resultado de eliminación.
     */
    fun resetDeleteResult() {
        deleteResult = null
    }
}
