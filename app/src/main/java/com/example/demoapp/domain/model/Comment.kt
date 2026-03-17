package com.example.demoapp.domain.model

/**
 * Entidad de dominio que representa un comentario en una publicación de mascota.
 * Los usuarios pueden agregar comentarios para aportar información adicional.
 */
data class Comment(
    val id: String,            // Identificador único del comentario
    val petId: String,         // ID de la publicación de mascota a la que pertenece
    val authorId: String,      // ID del usuario que escribió el comentario
    val authorName: String,    // Nombre del autor del comentario
    val text: String,          // Contenido del comentario
    val createdAt: Long = System.currentTimeMillis() // Fecha de creación en milisegundos
)
