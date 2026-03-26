package com.example.demoapp.domain.model

import java.util.Date
import java.util.UUID

/**
 * Modelo de datos para las notificaciones del sistema.
 * Representan avisos a los usuarios sobre acciones relevantes:
 * comentarios nuevos, cambios de estado, votos, etc.
 */
data class Notification(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,             // ID del usuario destinatario
    val title: String,              // Título de la notificación
    val message: String,            // Mensaje descriptivo
    val petId: String? = null,      // Publicación relacionada (opcional)
    val isRead: Boolean = false,    // Si fue leída
    val createdAt: Date = Date()    // Fecha de creación
)
