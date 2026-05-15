package com.example.demoapp.features.map

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
class MapViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    private val _verifiedPets = MutableStateFlow<List<Pet>>(emptyList())
    val verifiedPets: StateFlow<List<Pet>> = _verifiedPets.asStateFlow()

    init {
        loadVerifiedPets()
    }

    private fun loadVerifiedPets() {
        viewModelScope.launch {
            _verifiedPets.value = petRepository.getVerifiedPets()
        }
        // También observar cambios en tiempo real
        viewModelScope.launch {
            petRepository.pets.collect {
                _verifiedPets.value = petRepository.getVerifiedPets()
            }
        }
    }
}
