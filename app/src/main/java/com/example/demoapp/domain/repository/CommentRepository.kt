package com.example.demoapp.domain.repository

import com.example.demoapp.domain.model.Comment
import kotlinx.coroutines.flow.StateFlow

/**
 * Interfaz del repositorio de comentarios.
 * Todas las operaciones son suspend para Firestore.
 */
interface CommentRepository {
    val comments: StateFlow<List<Comment>>

    suspend fun getByPetId(petId: String): List<Comment>
    suspend fun add(comment: Comment)
    suspend fun delete(commentId: String): Boolean
}
