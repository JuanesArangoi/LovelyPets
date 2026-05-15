package com.example.demoapp.features.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.R
import com.example.demoapp.core.utils.ResourceProvider
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetStatus
import com.example.demoapp.domain.model.User
import com.example.demoapp.domain.repository.PetRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val petRepository: PetRepository,
    private val resources: ResourceProvider
) : ViewModel() {

    val name = ValidatedField("") { value ->
        when {
            value.isEmpty() -> resources.getString(R.string.error_name_empty)
            value.trim().length < 3 -> resources.getString(R.string.error_name_short)
            else -> null
        }
    }

    val address = ValidatedField("") { _ -> null }

    val phone = ValidatedField("") { value ->
        when {
            value.isEmpty() -> resources.getString(R.string.error_phone_empty)
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

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _userPets = MutableStateFlow<List<Pet>>(emptyList())
    val userPets: StateFlow<List<Pet>> = _userPets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getCurrentUser(): User? = _currentUser.value

    fun loadUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = userRepository.getCurrentUserId() ?: return@launch
            val user = userRepository.findById(userId)
            _currentUser.value = user
            user?.let {
                name.onChange(it.name)
                phone.onChange(it.phoneNumber)
                city.onChange(it.city)
                address.onChange(it.address)
            }
            _userPets.value = petRepository.getByOwner(userId)
            _isLoading.value = false
        }
    }

    fun getUserPets(): List<Pet> = _userPets.value

    fun getActivePetsCount(): Int {
        return _userPets.value.count { it.status != PetStatus.RESUELTO }
    }

    fun getResolvedPetsCount(): Int {
        return _userPets.value.count { it.status == PetStatus.RESUELTO }
    }

    fun getPendingPetsCount(): Int {
        return _userPets.value.count { it.status == PetStatus.PENDIENTE }
    }

    fun deletePet(petId: String) {
        viewModelScope.launch {
            petRepository.delete(petId)
            val userId = userRepository.getCurrentUserId() ?: return@launch
            _userPets.value = petRepository.getByOwner(userId)
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            val currentUser = _currentUser.value ?: return@launch
            val updatedUser = currentUser.copy(
                name = name.value,
                phoneNumber = phone.value,
                city = city.value,
                address = address.value
            )
            updateResult = userRepository.updateUser(updatedUser)
            if (updateResult == true) {
                _currentUser.value = updatedUser
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            val userId = _currentUser.value?.id ?: return@launch
            deleteResult = userRepository.deleteUser(userId)
        }
    }

    fun resetUpdateResult() { updateResult = null }
    fun resetDeleteResult() { deleteResult = null }
}
