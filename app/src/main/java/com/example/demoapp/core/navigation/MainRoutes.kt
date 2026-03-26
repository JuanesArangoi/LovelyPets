package com.example.demoapp.core.navigation

import kotlinx.serialization.Serializable

/**
 * Rutas principales de navegación de la aplicación.
 * Después de login, se usa UserDashboard o AdminDashboard que contienen
 * su propia navegación interna con BottomNavigationBar.
 */
sealed class MainRoutes {

    @Serializable
    data object Home : MainRoutes()          // Pantalla de bienvenida

    @Serializable
    data object Login : MainRoutes()         // Inicio de sesión

    @Serializable
    data object Register : MainRoutes()      // Registro de usuario

    @Serializable
    data object SendCode : MainRoutes()      // Enviar código de recuperación

    @Serializable
    data object VerifyCode : MainRoutes()    // Verificar código

    @Serializable
    data object ChangePassword : MainRoutes() // Cambiar contraseña

    @Serializable
    data object UserDashboard : MainRoutes()  // Dashboard del usuario (con BottomNav)

    @Serializable
    data object AdminDashboard : MainRoutes() // Dashboard del moderador (con BottomNav)
}
