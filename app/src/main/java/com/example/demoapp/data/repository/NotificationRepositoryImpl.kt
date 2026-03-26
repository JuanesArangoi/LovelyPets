package com.example.demoapp.data.repository

import com.example.demoapp.domain.model.Notification
import com.example.demoapp.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio de notificaciones.
 * Gestiona las notificaciones del sistema en memoria.
 */
@Singleton
class NotificationRepositoryImpl @Inject constructor() : NotificationRepository {

    // Lista reactiva de notificaciones
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    override val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    override fun getByUserId(userId: String): List<Notification> {
        return _notifications.value.filter { it.userId == userId }
    }

    override fun add(notification: Notification) {
        _notifications.value = _notifications.value + notification
    }

    override fun markAsRead(notificationId: String): Boolean {
        val index = _notifications.value.indexOfFirst { it.id == notificationId }
        if (index != -1) {
            val mutableList = _notifications.value.toMutableList()
            mutableList[index] = mutableList[index].copy(isRead = true)
            _notifications.value = mutableList
            return true
        }
        return false
    }

    override fun getUnreadCount(userId: String): Int {
        return _notifications.value.count { it.userId == userId && !it.isRead }
    }
}
