package com.example.demoapp.domain.repository

import com.example.demoapp.domain.model.Notification
import kotlinx.coroutines.flow.StateFlow

/**
 * Interfaz del repositorio de notificaciones.
 * Todas las operaciones son suspend para Firestore.
 */
interface NotificationRepository {
    val notifications: StateFlow<List<Notification>>

    suspend fun getByUserId(userId: String): List<Notification>
    suspend fun add(notification: Notification)
    suspend fun markAsRead(notificationId: String): Boolean
    suspend fun getUnreadCount(userId: String): Int
}
