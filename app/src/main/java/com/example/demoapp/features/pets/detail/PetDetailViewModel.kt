package com.example.demoapp.features.pets.detail

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.R
import com.example.demoapp.core.utils.ResourceProvider
import com.example.demoapp.domain.model.Comment
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.repository.CommentRepository
import com.example.demoapp.domain.repository.PetRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel para el detalle de una publicación de mascota.
 * Maneja la carga de datos, comentarios, votación, resolución,
 * contador de visualizaciones y compartir via Android Share Sheet.
 */
@HiltViewModel
class PetDetailViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val resources: ResourceProvider
) : ViewModel() {

    // Mascota cargada
    var pet by mutableStateOf<Pet?>(null)
        private set

    // Texto del nuevo comentario
    var commentText by mutableStateOf("")
        private set

    /**
     * Carga los datos de una mascota por su ID e incrementa el contador de visualizaciones.
     */
    fun loadPet(petId: String) {
        petRepository.incrementViewCount(petId)  // Incrementar cada vez que se abre el detalle
        pet = petRepository.findById(petId)
    }

    /**
     * Obtiene los comentarios de la publicación actual.
     */
    fun getComments(): List<Comment> {
        val petId = pet?.id ?: return emptyList()
        return commentRepository.getByPetId(petId)
    }

    /**
     * Actualiza el texto del nuevo comentario.
     */
    fun onCommentTextChange(text: String) {
        commentText = text
    }

    /**
     * Agrega un nuevo comentario a la publicación.
     */
    fun addComment() {
        val currentPet = pet ?: return
        val currentUser = userRepository.currentUser.value ?: return
        if (commentText.isBlank()) return

        val comment = Comment(
            id         = UUID.randomUUID().toString(),
            petId      = currentPet.id,
            authorId   = currentUser.id,
            authorName = currentUser.name,
            text       = commentText
        )
        commentRepository.add(comment)
        commentText = ""
    }

    /**
     * Vota por la publicación.
     */
    fun votePet() {
        val petId  = pet?.id ?: return
        val userId = userRepository.currentUser.value?.id ?: return
        petRepository.vote(petId, userId)
        pet = petRepository.findById(petId) // Recargar para actualizar votos
    }

    /**
     * Marca la publicación como resuelta.
     */
    fun resolvePet() {
        val petId = pet?.id ?: return
        petRepository.resolve(petId)
        pet = petRepository.findById(petId) // Recargar para actualizar estado
    }

    /**
     * Comparte la publicación usando el sistema de compartir de Android.
     * Permite compartir vía WhatsApp, correo, SMS, etc.
     */
    fun sharePet(context: Context) {
        val currentPet = pet ?: return
        val shareText = resources.getString(
            R.string.pet_share_text,
            currentPet.title,
            currentPet.description
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type    = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.pet_share_subject))
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(
            Intent.createChooser(intent, resources.getString(R.string.pet_detail_share))
        )
    }
}
