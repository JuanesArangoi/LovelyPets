package com.example.demoapp.features.pets.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.data.repository.PetRepository
import com.example.demoapp.domain.model.PetCategory

/**
 * ViewModel que gestiona la edición de una publicación de mascota existente.
 * Carga los datos actuales y permite editarlos.
 */
class EditPetViewModel : ViewModel() {

    // Campos validados del formulario
    val title = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El título es obligatorio"
            value.trim().length < 5 -> "El título debe tener al menos 5 caracteres"
            else -> null
        }
    }

    val description = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "La descripción es obligatoria"
            value.trim().length < 10 -> "La descripción debe tener al menos 10 caracteres"
            else -> null
        }
    }

    val animalType = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El tipo de animal es obligatorio"
            else -> null
        }
    }

    val breed = ValidatedField("") { _ -> null }

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

    // Categoría seleccionada
    var selectedCategory by mutableStateOf(PetCategory.ADOPCION)
        private set

    // Tiene vacunas
    var hasVaccines by mutableStateOf(false)
        private set

    // ID de la mascota que se está editando
    private var petId: String = ""

    // Resultado de la edición
    var editResult by mutableStateOf<Boolean?>(null)
        private set

    // Indica si el formulario es válido
    val isFormValid: Boolean
        get() = title.isValid
                && description.isValid
                && animalType.isValid
                && size.isValid
                && photoUrl.isValid

    /**
     * Carga los datos de la mascota para editar.
     */
    fun loadPet(id: String) {
        petId = id
        val pet = PetRepository.findById(id) ?: return

        // Cargar los valores actuales en los campos del formulario
        title.onChange(pet.title)
        description.onChange(pet.description)
        animalType.onChange(pet.animalType)
        breed.onChange(pet.breed)
        size.onChange(pet.size)
        photoUrl.onChange(pet.photoUrl)
        selectedCategory = pet.category
        hasVaccines = pet.hasVaccines
    }

    /**
     * Actualiza la categoría seleccionada.
     */
    fun onCategoryChange(category: PetCategory) {
        selectedCategory = category
    }

    /**
     * Actualiza el estado de vacunas.
     */
    fun onVaccinesChange(value: Boolean) {
        hasVaccines = value
    }

    /**
     * Guarda los cambios de la publicación.
     */
    fun savePet() {
        val existingPet = PetRepository.findById(petId) ?: return

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

        editResult = PetRepository.updatePet(updatedPet)
    }

    /**
     * Resetea el resultado de edición.
     */
    fun resetResult() {
        editResult = null
    }
}
