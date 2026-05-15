package com.example.demoapp.features.pets.create

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.model.Location
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory
import com.example.demoapp.domain.model.PetStatus
import com.example.demoapp.domain.repository.CloudinaryRepository
import com.example.demoapp.domain.repository.PetRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePetViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val userRepository: UserRepository,
    private val cloudinaryRepository: CloudinaryRepository
) : ViewModel() {

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
            value.isEmpty() -> "La foto es obligatoria"
            else -> null
        }
    }

    var selectedCategory by mutableStateOf(PetCategory.ADOPCION)
        private set

    var hasVaccines by mutableStateOf(false)
        private set

    var createResult by mutableStateOf<Boolean?>(null)
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isUploadingImage = MutableStateFlow(false)
    val isUploadingImage: StateFlow<Boolean> = _isUploadingImage.asStateFlow()

    // Ubicación seleccionada en el mapa
    var selectedLatitude by mutableStateOf(4.8133)
        private set
    var selectedLongitude by mutableStateOf(-75.6961)
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

    fun onLocationSelected(lat: Double, lng: Double) {
        selectedLatitude = lat
        selectedLongitude = lng
    }

    /**
     * Sube una imagen desde una URI local a Cloudinary y actualiza el campo photoUrl.
     */
    fun uploadImage(uri: Uri) {
        _isUploadingImage.value = true
        viewModelScope.launch {
            try {
                val url = cloudinaryRepository.uploadImage(uri)
                photoUrl.onChange(url)
            } catch (e: Exception) {
                // Mantener campo vacío si falla
            } finally {
                _isUploadingImage.value = false
            }
        }
    }

    fun createPet() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val userId = userRepository.getCurrentUserId()
                val user = if (userId != null) userRepository.findById(userId) else null

                if (user == null || !isFormValid) {
                    createResult = false
                    return@launch
                }

                val newPet = Pet(
                    title = title.value,
                    description = description.value,
                    category = selectedCategory,
                    status = PetStatus.PENDIENTE,
                    animalType = animalType.value,
                    size = size.value,
                    hasVaccines = hasVaccines,
                    photoUrl = photoUrl.value,
                    location = Location(selectedLatitude, selectedLongitude),
                    ownerId = user.id,
                    ownerName = user.name
                )

                petRepository.create(newPet)
                createResult = true
            } catch (e: Exception) {
                createResult = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetResult() {
        createResult = null
    }
}
