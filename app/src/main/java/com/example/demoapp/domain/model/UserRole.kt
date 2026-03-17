package com.example.demoapp.domain.model

/**
 * Roles de usuario en la aplicación.
 * Cada usuario tiene un rol que determina sus permisos y funcionalidades disponibles.
 */
enum class UserRole(val label: String) {
    USUARIO("Usuario"),         // Usuario normal que puede crear publicaciones y comentar
    MODERADOR("Moderador")      // Moderador que verifica o rechaza publicaciones
}
