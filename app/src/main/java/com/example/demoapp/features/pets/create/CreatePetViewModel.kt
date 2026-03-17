package com.example.demoapp.features.pets.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.data.SessionManager
import com.example.demoapp.data.repository.PetRepository
import com.example.demoapp.domain.model.Location
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory

/**
 * ViewModel que gestiona el formulario de creación de una nueva publicación de mascota.
 * Usa ValidatedField para la validación de campos del formulario.
 */
class CreatePetViewModel : ViewModel() {

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

    val breed = ValidatedField("") { value ->
        null // Campo opcional, sin validación
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

    // Categoría seleccionada
    var selectedCategory by mutableStateOf(PetCategory.ADOPCION)
        private set

    // Tiene vacunas (checkbox)
    var hasVaccines by mutableStateOf(false)
        private set

    // Resultado de la creación
    var createResult by mutableStateOf<Boolean?>(null)
        private set

    // Indica si el formulario es válido
    val isFormValid: Boolean
        get() = title.isValid
                && description.isValid
                && animalType.isValid
                && size.isValid
                && photoUrl.isValid

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
     * Crea una nueva publicación de mascota.
     */
    fun createPet() {
        val currentUser = SessionManager.currentUser
        if (currentUser == null) {
            createResult = false
            return
        }

        val newPet = Pet(
            id = "", // Se asignará en el repositorio
            title = title.value,
            description = description.value,
            category = selectedCategory,
            animalType = animalType.value,
            breed = breed.value,
            size = size.value,
            hasVaccines = hasVaccines,
            photoUrl = photoUrl.value,
            location = Location(4.8133, -75.6961), // Ubicación por defecto (Pereira)
            ownerId = currentUser.id,
            ownerName = currentUser.name
        )

        PetRepository.createPet(newPet)
        createResult = true
    }

    /**
     * Resetea el resultado de creación.
     */
    fun resetResult() {
        createResult = null
    }

    /**
     * Resetea todos los campos del formulario.
     */
    fun resetForm() {
        title.reset()
        description.reset()
        animalType.reset()
        breed.reset()
        size.reset()
        photoUrl.reset()
        selectedCategory = PetCategory.ADOPCION
        hasVaccines = false
        createResult = null
    }
}
