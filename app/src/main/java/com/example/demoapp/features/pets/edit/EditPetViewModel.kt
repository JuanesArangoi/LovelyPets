package com.example.demoapp.features.pets.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.model.PetCategory
import com.example.demoapp.domain.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para editar una publicación existente de mascota.
 * Pre-carga los datos de la publicación y maneja la actualización.
 */
@HiltViewModel
class EditPetViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    // Campos del formulario con validación
    val title = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El título es obligatorio"
            value.length < 5 -> "El título debe tener al menos 5 caracteres"
            else -> null
        }
    }

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

    // ID de la publicación que se está editando
    private var petId: String = ""

    val isFormValid: Boolean
        get() = title.isValid && description.isValid && animalType.isValid
                && size.isValid && photoUrl.isValid

    /**
     * Carga los datos de la publicación existente en los campos del formulario.
     */
    fun loadPet(id: String) {
        petId = id
        val pet = petRepository.findById(id) ?: return
        title.onChange(pet.title)
        description.onChange(pet.description)
        animalType.onChange(pet.animalType)
        size.onChange(pet.size)
        photoUrl.onChange(pet.photoUrl)
        selectedCategory = pet.category
        hasVaccines = pet.hasVaccines
    }

    fun onCategorySelected(category: PetCategory) {
        selectedCategory = category
    }

    fun onVaccinesChanged(value: Boolean) {
        hasVaccines = value
    }

    /**
     * Actualiza la publicación usando el PetRepository.
     */
    fun updatePet() {
        val existingPet = petRepository.findById(petId)
        if (existingPet == null || !isFormValid) {
            updateResult = false
            return
        }

        val updatedPet = existingPet.copy(
            title = title.value,
            description = description.value,
            category = selectedCategory,
            animalType = animalType.value,
            size = size.value,
            hasVaccines = hasVaccines,
            photoUrl = photoUrl.value
        )

        updateResult = petRepository.update(updatedPet)
    }

    fun resetResult() {
        updateResult = null
    }
}
