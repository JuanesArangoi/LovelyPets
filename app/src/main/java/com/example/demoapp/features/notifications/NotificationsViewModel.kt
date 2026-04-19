package com.example.demoapp.features.notifications

import androidx.lifecycle.ViewModel
import com.example.demoapp.domain.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications = _notifications.asStateFlow()

    init {
        loadMockNotifications()
    }

    private fun loadMockNotifications() {
        // Datos de ejemplo para visualizar la interfaz
        _notifications.value = listOf(
            Notification(
                userId = "1",
                title = "¡Nueva adopción!",
                message = "Alguien está interesado en adoptar a Toby.",
                isRead = false
            ),
            Notification(
                userId = "1",
                title = "Comentario nuevo",
                message = "Juan ha comentado en tu publicación de Luna.",
                isRead = true
            ),
            Notification(
                userId = "1",
                title = "Mascota verificada",
                message = "Tu publicación de 'Gato perdido' ha sido aprobada por un moderador.",
                isRead = false
            )
        )
    }

    fun markAsRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }
}
