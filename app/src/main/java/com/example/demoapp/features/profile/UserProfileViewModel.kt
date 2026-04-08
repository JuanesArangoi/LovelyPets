package com.example.demoapp.features.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.model.User
import com.example.demoapp.domain.model.PetStatus
import com.example.demoapp.domain.repository.PetRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para el perfil del usuario.
 * Maneja la edición de datos, estadísticas y eliminación de cuenta.
 */
@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val petRepository: PetRepository
) : ViewModel() {

    // Campos editables del perfil
    val name = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El nombre es obligatorio"
            value.trim().length < 3 -> "Ingresa un nombre válido"
            else -> null
        }
    }

    val address = ValidatedField("") { _ -> null } // Dirección es opcional

    val phone = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El teléfono es obligatorio"
            value.length < 7 -> "Ingresa un teléfono válido"
            else -> null
        }
    }

    val city = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "La ciudad es obligatoria"
            else -> null
        }
    }

    // Resultado de las operaciones
    var updateResult by mutableStateOf<Boolean?>(null)
        private set

    var deleteResult by mutableStateOf<Boolean?>(null)
        private set

    /**
     * Obtiene el usuario actual desde el repositorio.
     */
    fun getCurrentUser(): User? {
        return userRepository.currentUser.value
    }

    /**
     * Carga los datos del usuario actual en los campos editables.
     */
    fun loadUserData() {
        val user = userRepository.currentUser.value ?: return
        name.onChange(user.name)
        phone.onChange(user.phoneNumber)
        city.onChange(user.city)
        address.onChange(user.address)
    }

    /**
     * Obtiene el número de mascotas activas del usuario.
     */
    fun getActivePetsCount(): Int {
        val userId = userRepository.currentUser.value?.id ?: return 0
        return petRepository.getByOwner(userId).count { it.status == PetStatus.VERIFICADO }
    }

    /**
     * Obtiene el número de mascotas resueltas del usuario.
     */
    fun getResolvedPetsCount(): Int {
        val userId = userRepository.currentUser.value?.id ?: return 0
        return petRepository.getByOwner(userId).count { it.status == PetStatus.RESUELTO }
    }

    /**
     * Obtiene el número de mascotas pendientes del usuario.
     */
    fun getPendingPetsCount(): Int {
        val userId = userRepository.currentUser.value?.id ?: return 0
        return petRepository.getByOwner(userId).count { it.status == PetStatus.PENDIENTE }
    }

    /**
     * Actualiza los datos del perfil usando el UserRepository.
     */
    fun updateProfile() {
        val currentUser = userRepository.currentUser.value ?: return
        val updatedUser = currentUser.copy(
            name = name.value,
            phoneNumber = phone.value,
            city = city.value,
            address = address.value
        )
        updateResult = userRepository.updateUser(updatedUser)
    }

    /**
     * Elimina la cuenta del usuario actual.
     */
    fun deleteAccount() {
        val userId = userRepository.currentUser.value?.id ?: return
        deleteResult = userRepository.deleteUser(userId)
    }

    fun resetUpdateResult() {
        updateResult = null
    }

    fun resetDeleteResult() {
        deleteResult = null
    }
}
