package com.example.demoapp.features.pets.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.model.Location
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory
import com.example.demoapp.domain.model.PetStatus
import com.example.demoapp.domain.repository.PetRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para la creación de una nueva publicación de mascota.
 * Implementa validación de formulario con ValidatedField.
 */
@HiltViewModel
class CreatePetViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val userRepository: UserRepository
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

    // Estado de la categoría seleccionada
    var selectedCategory by mutableStateOf(PetCategory.ADOPCION)
        private set

    // Estado de vacunación
    var hasVaccines by mutableStateOf(false)
        private set

    // Resultado de la creación
    var createResult by mutableStateOf<Boolean?>(null)
        private set

    val isFormValid: Boolean
        get() = title.isValid && description.isValid && animalType.isValid
                && size.isValid && photoUrl.isValid

    fun onCategorySelected(category: PetCategory) {
        selectedCategory = category
    }

    fun onVaccinesChanged(value: Boolean) {
        hasVaccines = value
    }

    /**
     * Crea la publicación usando el PetRepository.
     */
    fun createPet() {
        val currentUser = userRepository.currentUser.value
        if (currentUser == null || !isFormValid) {
            createResult = false
            return
        }

        // Se pasa explícitamente el id como vacío y breed si es necesario 
        // para evitar errores de parámetros faltantes en algunas versiones de Kotlin/KSP.
        val newPet = Pet(
            id = "", 
            title = title.value,
            description = description.value,
            category = selectedCategory,
            status = PetStatus.PENDIENTE,
            animalType = animalType.value,
            breed = "",
            size = size.value,
            hasVaccines = hasVaccines,
            photoUrl = photoUrl.value,
            location = Location(4.8133, -75.6961),
            ownerId = currentUser.id,
            ownerName = currentUser.name
        )

        petRepository.create(newPet)
        createResult = true
    }

    fun resetResult() {
        createResult = null
    }
}
