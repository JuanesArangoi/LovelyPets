package com.example.demoapp.features.dashboard.navigation

import kotlinx.serialization.Serializable

/**
 * Rutas de navegación internas del dashboard.
 * Estas rutas se usan dentro del NavHost anidado de la sección de usuarios/moderadores.
 * Son independientes de las rutas principales (MainRoutes).
 */
sealed class DashboardRoutes {

    // === Rutas comunes (usuarios y moderadores) ===
    @Serializable
    data object PetFeed : DashboardRoutes()       // Feed de mascotas

    @Serializable
    data object Profile : DashboardRoutes()       // Perfil del usuario

    @Serializable
    data class PetDetail(val petId: String) : DashboardRoutes()  // Detalle de mascota

    @Serializable
    data class EditPet(val petId: String) : DashboardRoutes()    // Editar mascota

    @Serializable
    data object CreatePet : DashboardRoutes()     // Crear publicación

    // === Ruta exclusiva del moderador ===
    @Serializable
    data object ModeratorPanel : DashboardRoutes() // Panel de moderación
}
