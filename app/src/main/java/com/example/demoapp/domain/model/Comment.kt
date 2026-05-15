package com.example.demoapp.domain.model

/**
 * Entidad de dominio que representa un comentario en una publicación de mascota.
 * Valores por defecto para compatibilidad con Firestore.
 */
data class Comment(
    var id: String = "",
    val petId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val text: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
