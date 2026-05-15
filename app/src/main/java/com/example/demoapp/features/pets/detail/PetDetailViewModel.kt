package com.example.demoapp.features.pets.detail

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.R
import com.example.demoapp.core.utils.ResourceProvider
import com.example.demoapp.domain.model.Comment
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.repository.CommentRepository
import com.example.demoapp.domain.repository.PetRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para el detalle de una publicación de mascota.
 * Migrado a coroutines para compatibilidad con Firestore.
 */
@HiltViewModel
class PetDetailViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val resources: ResourceProvider
) : ViewModel() {

    var pet by mutableStateOf<Pet?>(null)
        private set

    var commentText by mutableStateOf("")
        private set

    var deleteResult by mutableStateOf<Boolean?>(null)
        private set

    var commentsList by mutableStateOf<List<Comment>>(emptyList())
        private set

    val isOwner: Boolean
        get() {
            val userId = userRepository.getCurrentUserId() ?: return false
            return pet?.ownerId == userId
        }

    fun loadPet(petId: String) {
        viewModelScope.launch {
            petRepository.incrementViewCount(petId)
            pet = petRepository.findById(petId)
            commentsList = commentRepository.getByPetId(petId)
        }
    }

    fun getComments(): List<Comment> = commentsList

    fun onCommentTextChange(text: String) {
        commentText = text
    }

    fun addComment() {
        val currentPet = pet ?: return
        val userId = userRepository.getCurrentUserId() ?: return
        if (commentText.isBlank()) return

        viewModelScope.launch {
            val user = userRepository.findById(userId)
            val comment = Comment(
                petId = currentPet.id,
                authorId = userId,
                authorName = user?.name ?: "Usuario",
                text = commentText
            )
            commentRepository.add(comment)
            commentText = ""
            commentsList = commentRepository.getByPetId(currentPet.id)
        }
    }

    fun votePet() {
        val petId = pet?.id ?: return
        val userId = userRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            petRepository.vote(petId, userId)
            pet = petRepository.findById(petId)
        }
    }

    fun resolvePet() {
        val petId = pet?.id ?: return
        viewModelScope.launch {
            petRepository.resolve(petId)
            pet = petRepository.findById(petId)
        }
    }

    fun deletePet() {
        val petId = pet?.id ?: return
        viewModelScope.launch {
            deleteResult = petRepository.delete(petId)
        }
    }

    fun resetDeleteResult() { deleteResult = null }

    fun sharePet(context: Context) {
        val currentPet = pet ?: return
        val shareText = resources.getString(
            R.string.pet_share_text,
            currentPet.title,
            currentPet.description
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.pet_share_subject))
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(
            Intent.createChooser(intent, resources.getString(R.string.pet_detail_share))
        )
    }
}
