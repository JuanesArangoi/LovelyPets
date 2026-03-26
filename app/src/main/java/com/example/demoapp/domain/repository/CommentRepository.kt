package com.example.demoapp.domain.repository

import com.example.demoapp.domain.model.Comment
import kotlinx.coroutines.flow.StateFlow

/**
 * Interfaz del repositorio de comentarios.
 * Define las operaciones para gestionar los comentarios
 * asociados a las publicaciones de mascotas.
 */
interface CommentRepository {
    val comments: StateFlow<List<Comment>>

    fun getByPetId(petId: String): List<Comment>
    fun add(comment: Comment)
    fun delete(commentId: String): Boolean
}
