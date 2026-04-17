package com.example.demoapp.data.model

import com.example.demoapp.domain.model.UserRole

/**
 * Modelo de datos que representa la sesión del usuario almacenada en DataStore.
 * Contiene el ID del usuario y su rol para determinar el flujo de navegación.
 */
data class UserSession(
    val userId: String,
    val role: UserRole
)
