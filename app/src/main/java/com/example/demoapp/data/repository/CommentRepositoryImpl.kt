package com.example.demoapp.data.repository

import com.example.demoapp.domain.model.Comment
import com.example.demoapp.domain.repository.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio de comentarios.
 * Gestiona los comentarios de las publicaciones de mascotas en memoria.
 */
@Singleton
class CommentRepositoryImpl @Inject constructor() : CommentRepository {

    // Lista reactiva de comentarios
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    override val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    init {
        _comments.value = loadSampleComments()
    }

    override fun getByPetId(petId: String): List<Comment> {
        return _comments.value.filter { it.petId == petId }
    }

    override fun add(comment: Comment) {
        _comments.value = _comments.value + comment
    }

    override fun delete(commentId: String): Boolean {
        val sizeBefore = _comments.value.size
        _comments.value = _comments.value.filter { it.id != commentId }
        return _comments.value.size < sizeBefore
    }

    /**
     * Carga comentarios de ejemplo.
     */
    private fun loadSampleComments(): List<Comment> {
        return listOf(
            Comment(
                id = "c1",
                petId = "1",
                authorId = "2",
                authorName = "María López",
                text = "¡Qué hermosa! Me gustaría saber más sobre ella."
            ),
            Comment(
                id = "c2",
                petId = "3",
                authorId = "2",
                authorName = "María López",
                text = "Vi un perro similar cerca de la universidad, revisaré mañana."
            ),
            Comment(
                id = "c3",
                petId = "3",
                authorId = "1",
                authorName = "Juan Arango",
                text = "¡Gracias! Cualquier información es valiosa."
            )
        )
    }
}
