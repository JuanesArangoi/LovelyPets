package com.example.demoapp.features.moderator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModeratorViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    var rejectionReason by mutableStateOf("")
        private set

    private val _pendingPets = MutableStateFlow<List<Pet>>(emptyList())
    val pendingPets: StateFlow<List<Pet>> = _pendingPets.asStateFlow()

    init {
        loadPendingPets()
        viewModelScope.launch {
            petRepository.pets.collect {
                _pendingPets.value = petRepository.getPendingPets()
            }
        }
    }

    private fun loadPendingPets() {
        viewModelScope.launch {
            _pendingPets.value = petRepository.getPendingPets()
        }
    }

    fun getPendingPets(): List<Pet> = _pendingPets.value

    fun onRejectionReasonChange(reason: String) {
        rejectionReason = reason
    }

    fun verifyPet(petId: String) {
        viewModelScope.launch {
            petRepository.verify(petId)
        }
    }

    fun rejectPet(petId: String) {
        if (rejectionReason.isNotBlank()) {
            viewModelScope.launch {
                petRepository.reject(petId, rejectionReason)
                rejectionReason = ""
            }
        }
    }

    fun clearRejectionReason() {
        rejectionReason = ""
    }
}
