package com.example.demoapp.domain.model

/**
 * Modelo de datos para las notificaciones del sistema.
 * Valores por defecto para compatibilidad con Firestore.
 */
data class Notification(
    var id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val petId: String? = null,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
