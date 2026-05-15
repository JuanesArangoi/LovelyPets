package com.example.demoapp.features.pets.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory
import com.example.demoapp.domain.repository.PetRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetListViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var selectedCategory by mutableStateOf<PetCategory?>(null)
        private set

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()

    init {
        loadPets()
        // Observe real-time changes
        viewModelScope.launch {
            petRepository.pets.collect {
                refreshPets()
            }
        }
    }

    private fun loadPets() {
        viewModelScope.launch {
            refreshPets()
        }
    }

    private suspend fun refreshPets() {
        _pets.value = if (selectedCategory != null) {
            petRepository.getByCategory(selectedCategory!!)
        } else {
            petRepository.getVerifiedPets()
        }
    }

    fun getPets(): List<Pet> = _pets.value

    fun onCategorySelected(category: PetCategory?) {
        selectedCategory = category
        viewModelScope.launch {
            refreshPets()
        }
    }

    fun votePet(petId: String) {
        val userId = userRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            petRepository.vote(petId, userId)
        }
    }

    fun deletePet(petId: String) {
        viewModelScope.launch {
            petRepository.delete(petId)
        }
    }
}
