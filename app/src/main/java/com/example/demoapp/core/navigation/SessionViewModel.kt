package com.example.demoapp.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.data.datastore.SessionDataStore
import com.example.demoapp.data.model.UserSession
import com.example.demoapp.domain.model.UserRole
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Posibles estados de la sesión del usuario.
 */
sealed interface SessionState {
    data object Loading : SessionState
    data object NotAuthenticated : SessionState
    data class Authenticated(val session: UserSession) : SessionState
}

/**
 * ViewModel que gestiona el estado global de la sesión.
 * Integra DataStore para persistencia y Firebase Auth para signOut.
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val userRepository: UserRepository
) : ViewModel() {

    val sessionState: StateFlow<SessionState> = sessionDataStore.sessionFlow
        .map { session ->
            if (session != null) SessionState.Authenticated(session)
            else SessionState.NotAuthenticated
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SessionState.Loading
        )

    fun login(userId: String, role: UserRole) {
        viewModelScope.launch {
            sessionDataStore.saveSession(userId, role)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.signOut() // Firebase Auth sign out
            sessionDataStore.clearSession()
        }
    }
}
