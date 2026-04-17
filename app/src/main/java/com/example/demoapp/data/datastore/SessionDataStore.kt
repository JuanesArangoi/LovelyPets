package com.example.demoapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.demoapp.data.model.UserSession
import com.example.demoapp.domain.model.UserRole
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Extension property para obtener el DataStore de preferencias de sesión.
 * Se crea una única instancia por aplicación.
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

/**
 * Clase Singleton que gestiona el almacenamiento de la sesión del usuario
 * usando Jetpack DataStore (sustituye el antiguo SessionManager en memoria).
 */
@Singleton
class SessionDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    // Claves para las preferencias
    private object Keys {
        val USER_ID = stringPreferencesKey("user_id")
        val ROLE    = stringPreferencesKey("role")
    }

    /**
     * Flujo que emite el estado actual de la sesión.
     * Emite null si el usuario no está autenticado.
     */
    val sessionFlow: Flow<UserSession?> = context.dataStore.data.map { prefs ->
        val userId  = prefs[Keys.USER_ID]
        val roleStr = prefs[Keys.ROLE]
        if (userId.isNullOrBlank() || roleStr.isNullOrBlank()) {
            null
        } else {
            UserSession(
                userId = userId,
                role   = UserRole.valueOf(roleStr)
            )
        }
    }

    /**
     * Guarda los datos de sesión en DataStore.
     */
    suspend fun saveSession(userId: String, role: UserRole) {
        context.dataStore.edit { prefs ->
            prefs[Keys.USER_ID] = userId
            prefs[Keys.ROLE]    = role.name
        }
    }

    /**
     * Limpia todos los datos de sesión (logout).
     */
    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
