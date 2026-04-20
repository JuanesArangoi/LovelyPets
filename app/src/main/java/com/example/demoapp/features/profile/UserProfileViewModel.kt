package com.example.demoapp.features.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.R
import com.example.demoapp.core.utils.ResourceProvider
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetStatus
import com.example.demoapp.domain.model.User
import com.example.demoapp.domain.repository.PetRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val petRepository: PetRepository,
    private val resources: ResourceProvider
) : ViewModel() {

    val name = ValidatedField("") { value ->
        when {
            value.isEmpty()        -> resources.getString(R.string.error_name_empty)
            value.trim().length < 3 -> resources.getString(R.string.error_name_short)
            else -> null
        }
    }

    val address = ValidatedField("") { _ -> null }

    val phone = ValidatedField("") { value ->
        when {
            value.isEmpty()  -> resources.getString(R.string.error_phone_empty)
            value.length < 7 -> resources.getString(R.string.error_phone_short)
            else -> null
        }
    }

    val city = ValidatedField("") { value ->
        when {
            value.isEmpty() -> resources.getString(R.string.error_city_empty)
            else -> null
        }
    }

    var updateResult by mutableStateOf<Boolean?>(null)
        private set

    var deleteResult by mutableStateOf<Boolean?>(null)
        private set

    fun getCurrentUser(): User? = userRepository.currentUser.value

    fun loadUserData() {
        val user = userRepository.currentUser.value ?: return
        name.onChange(user.name)
        phone.onChange(user.phoneNumber)
        city.onChange(user.city)
        address.onChange(user.address)
    }

    // Obtener las publicaciones del usuario actual
    fun getUserPets(): List<Pet> {
        val userId = userRepository.currentUser.value?.id ?: return emptyList()
        return petRepository.getByOwner(userId)
    }

    fun getActivePetsCount(): Int {
        val userId = userRepository.currentUser.value?.id ?: return 0
        return petRepository.getByOwner(userId).count {
            it.status != PetStatus.RESUELTO
        }
    }
    fun getResolvedPetsCount(): Int {
        val userId = userRepository.currentUser.value?.id ?: return 0
        return petRepository.getByOwner(userId).count { it.status == PetStatus.RESUELTO }
    }

    fun getPendingPetsCount(): Int {
        val userId = userRepository.currentUser.value?.id ?: return 0
        return petRepository.getByOwner(userId).count { it.status == PetStatus.PENDIENTE }
    }

    fun deletePet(petId: String) {
        petRepository.delete(petId)
    }

    fun updateProfile() {
        val currentUser = userRepository.currentUser.value ?: return
        val updatedUser = currentUser.copy(
            name        = name.value,
            phoneNumber = phone.value,
            city        = city.value,
            address     = address.value
        )
        updateResult = userRepository.updateUser(updatedUser)
    }

    fun deleteAccount() {
        val userId = userRepository.currentUser.value?.id ?: return
        deleteResult = userRepository.deleteUser(userId)
    }

    fun resetUpdateResult() { updateResult = null }
    fun resetDeleteResult() { deleteResult = null }
}
