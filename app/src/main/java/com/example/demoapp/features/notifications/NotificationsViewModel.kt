package com.example.demoapp.features.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.domain.model.Notification
import com.example.demoapp.domain.repository.NotificationRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications = _notifications.asStateFlow()

    init {
        loadNotifications()
        // Observe real-time changes
        viewModelScope.launch {
            notificationRepository.notifications.collect {
                val userId = userRepository.getCurrentUserId() ?: return@collect
                _notifications.value = notificationRepository.getByUserId(userId)
            }
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId() ?: return@launch
            _notifications.value = notificationRepository.getByUserId(userId)
        }
    }

    fun markAsRead(id: String) {
        viewModelScope.launch {
            notificationRepository.markAsRead(id)
        }
    }
}
