package com.example.demoapp.core.navigation

import kotlinx.serialization.Serializable

/**
 * Define todas las rutas de navegación de la aplicación.
 * Usa sealed class para un conjunto cerrado de rutas con serialización.
 */
sealed class MainRoutes {

    // Pantalla de bienvenida (inicio de la aplicación)
    @Serializable
    data object Home : MainRoutes()

    // Pantalla de inicio de sesión
    @Serializable
    data object Login : MainRoutes()

    // Pantalla de registro de nuevo usuario
    @Serializable
    data object Register : MainRoutes()

    // Pantalla para enviar código de recuperación de contraseña
    @Serializable
    data object SendCode : MainRoutes()

    // Pantalla para verificar el código de recuperación
    @Serializable
    data object VerifyCode : MainRoutes()

    // Pantalla para cambiar la contraseña
    @Serializable
    data object ChangePassword : MainRoutes()

    // Feed principal de publicaciones de mascotas
    @Serializable
    data object PetList : MainRoutes()

    // Detalle de una publicación de mascota (recibe el ID como parámetro)
    @Serializable
    data class PetDetail(val petId: String) : MainRoutes()

    // Pantalla para crear una nueva publicación de mascota
    @Serializable
    data object CreatePet : MainRoutes()

    // Pantalla para editar una publicación existente (recibe el ID como parámetro)
    @Serializable
    data class EditPet(val petId: String) : MainRoutes()

    // Pantalla de perfil del usuario
    @Serializable
    data object UserProfile : MainRoutes()

    // Panel del moderador
    @Serializable
    data object ModeratorPanel : MainRoutes()
}
