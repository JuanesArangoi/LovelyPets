package com.example.demoapp.features.pets.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory
import com.example.demoapp.domain.repository.PetRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para el feed de publicaciones de mascotas.
 * Usa @HiltViewModel para inyectar los repositorios via Hilt.
 */
@HiltViewModel
class PetListViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // Categoría seleccionada para filtrar (null = todas)
    var selectedCategory by mutableStateOf<PetCategory?>(null)
        private set

    /**
     * Obtiene la lista de mascotas verificadas, filtradas por categoría si aplica.
     */
    fun getPets(): List<Pet> {
        return if (selectedCategory != null) {
            petRepository.getByCategory(selectedCategory!!)
        } else {
            petRepository.getVerifiedPets()
        }
    }

    /**
     * Cambia la categoría seleccionada para filtrar.
     */
    fun onCategorySelected(category: PetCategory?) {
        selectedCategory = category
    }

    /**
     * Vota por una publicación.
     */
    fun votePet(petId: String) {
        val userId = userRepository.currentUser.value?.id ?: return
        petRepository.vote(petId, userId)
    }

    /**
     * Elimina una publicación (solo el dueño o moderador).
     */
    fun deletePet(petId: String) {
        petRepository.delete(petId)
    }
}
