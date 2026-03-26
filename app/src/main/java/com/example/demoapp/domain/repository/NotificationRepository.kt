package com.example.demoapp.domain.repository

import com.example.demoapp.domain.model.Notification
import kotlinx.coroutines.flow.StateFlow

/**
 * Interfaz del repositorio de notificaciones.
 * Define las operaciones para gestionar las notificaciones del sistema.
 */
interface NotificationRepository {
    val notifications: StateFlow<List<Notification>>

    fun getByUserId(userId: String): List<Notification>
    fun add(notification: Notification)
    fun markAsRead(notificationId: String): Boolean
    fun getUnreadCount(userId: String): Int
}
