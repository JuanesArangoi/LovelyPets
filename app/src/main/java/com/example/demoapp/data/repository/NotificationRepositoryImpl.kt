package com.example.demoapp.data.repository

import com.example.demoapp.domain.model.Notification
import com.example.demoapp.domain.repository.NotificationRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : NotificationRepository {

    private val collection = firestore.collection("notifications")

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    override val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    init {
        collection.addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                _notifications.value = it.documents.mapNotNull { snap ->
                    snap.toObject(Notification::class.java)?.apply { id = snap.id }
                }
            }
        }
    }

    override suspend fun getByUserId(userId: String): List<Notification> {
        return _notifications.value.filter { it.userId == userId }
    }

    override suspend fun add(notification: Notification) {
        collection.add(notification).await()
    }

    override suspend fun markAsRead(notificationId: String): Boolean {
        return try {
            collection.document(notificationId).update("isRead", true).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getUnreadCount(userId: String): Int {
        return _notifications.value.count { it.userId == userId && !it.isRead }
    }
}
