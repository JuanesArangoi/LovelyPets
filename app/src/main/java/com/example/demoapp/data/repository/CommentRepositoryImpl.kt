package com.example.demoapp.data.repository

import com.example.demoapp.domain.model.Comment
import com.example.demoapp.domain.repository.CommentRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CommentRepository {

    private val collection = firestore.collection("comments")

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    override val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    init {
        collection.addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                _comments.value = it.documents.mapNotNull { snap ->
                    snap.toObject(Comment::class.java)?.apply { id = snap.id }
                }
            }
        }
    }

    override suspend fun getByPetId(petId: String): List<Comment> {
        return _comments.value.filter { it.petId == petId }
    }

    override suspend fun add(comment: Comment) {
        collection.add(comment).await()
    }

    override suspend fun delete(commentId: String): Boolean {
        return try {
            collection.document(commentId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
