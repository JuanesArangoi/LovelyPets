package com.example.demoapp.domain.model

/**
 * Entidad de dominio que representa un usuario de la aplicación.
 * Contiene toda la información personal, rol, nivel y puntos del usuario.
 */
data class User(
    val id: String,                                      // Identificador único del usuario
    val name: String,                                    // Nombre completo del usuario
    val email: String,                                   // Correo electrónico del usuario
    val password: String,                                // Contraseña del usuario
    val phoneNumber: String = "",                        // Número de teléfono (opcional)
    val city: String = "",                               // Ciudad de residencia
    val address: String = "",                            // Dirección del usuario (opcional)
    val role: UserRole = UserRole.USUARIO,               // Rol del usuario (USUARIO o MODERADOR)
    val level: UserLevel = UserLevel.AMIGO_ANIMAL,       // Nivel del usuario según puntos
    val points: Int = 0,                                 // Puntos acumulados por participación
    val profilePictureUrl: String = ""                   // URL de la foto de perfil (opcional)
)
