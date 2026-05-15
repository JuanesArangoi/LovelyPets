package com.example.demoapp.features.pets.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.model.PetCategory
import com.example.demoapp.domain.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPetViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    val title = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El título es obligatorio"
            value.length < 5 -> "El título debe tener al menos 5 caracteres"
            else -> null
        }
    }

    val breed = ValidatedField("") { _ -> null }

    val description = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "La descripción es obligatoria"
            value.length < 20 -> "La descripción debe tener al menos 20 caracteres"
            else -> null
        }
    }

    val animalType = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El tipo de animal es obligatorio"
            else -> null
        }
    }

    val size = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El tamaño es obligatorio"
            else -> null
        }
    }

    val photoUrl = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "La URL de la foto es obligatoria"
            !value.startsWith("http") -> "Ingresa una URL válida"
            else -> null
        }
    }

    var selectedCategory by mutableStateOf(PetCategory.ADOPCION)
        private set

    var hasVaccines by mutableStateOf(false)
        private set

    var updateResult by mutableStateOf<Boolean?>(null)
        private set

    private var petId: String = ""

    val isFormValid: Boolean
        get() = title.isValid && description.isValid && animalType.isValid
                && size.isValid && photoUrl.isValid

    fun loadPet(id: String) {
        petId = id
        viewModelScope.launch {
            val pet = petRepository.findById(id) ?: return@launch
            title.onChange(pet.title)
            description.onChange(pet.description)
            animalType.onChange(pet.animalType)
            breed.onChange(pet.breed)
            size.onChange(pet.size)
            photoUrl.onChange(pet.photoUrl)
            selectedCategory = pet.category
            hasVaccines = pet.hasVaccines
        }
    }

    fun onCategorySelected(category: PetCategory) {
        selectedCategory = category
    }

    fun onVaccinesChanged(value: Boolean) {
        hasVaccines = value
    }

    fun updatePet() {
        viewModelScope.launch {
            val existingPet = petRepository.findById(petId)
            if (existingPet == null || !isFormValid) {
                updateResult = false
                return@launch
            }

            val updatedPet = existingPet.copy(
                title = title.value,
                description = description.value,
                category = selectedCategory,
                animalType = animalType.value,
                breed = breed.value,
                size = size.value,
                hasVaccines = hasVaccines,
                photoUrl = photoUrl.value
            )

            updateResult = petRepository.update(updatedPet)
        }
    }

    fun resetResult() {
        updateResult = null
    }
}
