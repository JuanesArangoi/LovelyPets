package com.example.demoapp.features.pets.list

import androidx.lifecycle.ViewModel
import com.example.demoapp.data.repository.PetRepository
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel que gestiona el estado del feed de publicaciones de mascotas.
 * Maneja la lista de mascotas, filtros por categoría y acciones como votar o eliminar.
 */
class PetListViewModel : ViewModel() {

    // Lista de mascotas filtrada (la que se muestra en pantalla)
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()

    // Categoría seleccionada para filtrar (null = todas)
    private val _selectedCategory = MutableStateFlow<PetCategory?>(null)
    val selectedCategory: StateFlow<PetCategory?> = _selectedCategory.asStateFlow()

    // Inicializar cargando todas las mascotas verificadas
    init {
        loadPets()
    }

    /**
     * Carga las mascotas verificadas del repositorio.
     * Si hay una categoría seleccionada, filtra por ella.
     */
    fun loadPets() {
        val category = _selectedCategory.value
        _pets.value = if (category != null) {
            PetRepository.getVerifiedPetsByCategory(category)
        } else {
            PetRepository.getVerifiedPets()
        }
    }

    /**
     * Filtra las publicaciones por categoría.
     * Si se selecciona la misma categoría que ya estaba, se limpia el filtro.
     */
    fun filterByCategory(category: PetCategory?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
        loadPets()
    }

    /**
     * Agrega un voto "Me interesa" a una publicación.
     */
    fun votePet(petId: String) {
        PetRepository.votePet(petId)
        loadPets() // Recargar para reflejar el cambio
    }

    /**
     * Elimina una publicación (solo el dueño puede hacerlo).
     */
    fun deletePet(petId: String) {
        PetRepository.deletePet(petId)
        loadPets() // Recargar para reflejar el cambio
    }
}
