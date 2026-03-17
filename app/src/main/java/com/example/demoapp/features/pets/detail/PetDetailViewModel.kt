package com.example.demoapp.features.pets.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.data.SessionManager
import com.example.demoapp.data.repository.PetRepository
import com.example.demoapp.domain.model.Comment
import com.example.demoapp.domain.model.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * ViewModel que gestiona el estado del detalle de una publicación de mascota.
 * Carga la mascota por ID, permite agregar comentarios y votar.
 */
class PetDetailViewModel : ViewModel() {

    // Publicación de mascota actual
    private val _pet = MutableStateFlow<Pet?>(null)
    val pet: StateFlow<Pet?> = _pet.asStateFlow()

    // Texto del nuevo comentario
    var newCommentText by mutableStateOf("")
        private set

    /**
     * Carga una mascota por su ID desde el repositorio.
     */
    fun loadPet(petId: String) {
        _pet.value = PetRepository.findById(petId)
    }

    /**
     * Actualiza el texto del nuevo comentario.
     */
    fun onCommentTextChange(text: String) {
        newCommentText = text
    }

    /**
     * Agrega un nuevo comentario a la publicación actual.
     */
    fun addComment() {
        val currentUser = SessionManager.currentUser ?: return
        val petId = _pet.value?.id ?: return

        if (newCommentText.isBlank()) return

        val comment = Comment(
            id = UUID.randomUUID().toString(),
            petId = petId,
            authorId = currentUser.id,
            authorName = currentUser.name,
            text = newCommentText
        )

        PetRepository.addComment(petId, comment)
        newCommentText = "" // Limpiar campo de texto
        loadPet(petId) // Recargar para mostrar el nuevo comentario
    }

    /**
     * Agrega un voto "Me interesa" a la publicación.
     */
    fun votePet() {
        val petId = _pet.value?.id ?: return
        PetRepository.votePet(petId)
        loadPet(petId) // Recargar para reflejar el cambio
    }

    /**
     * Marca la publicación como resuelta/finalizada.
     */
    fun resolvePet() {
        val petId = _pet.value?.id ?: return
        PetRepository.resolvePet(petId)
        loadPet(petId) // Recargar para reflejar el cambio
    }
}
