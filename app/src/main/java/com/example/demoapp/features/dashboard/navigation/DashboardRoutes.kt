package com.example.demoapp.features.dashboard.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class DashboardRoutes {

    @Serializable
    data object PetFeed : DashboardRoutes()

    @Serializable
    data object Profile : DashboardRoutes()
    
    @Serializable
    data object Map : DashboardRoutes()

    @Serializable
    data object Notifications : DashboardRoutes() // NUEVA RUTA

    @Serializable
    data class PetDetail(val petId: String) : DashboardRoutes()

    @Serializable
    data class EditPet(val petId: String) : DashboardRoutes()

    @Serializable
    data object CreatePet : DashboardRoutes()

    @Serializable
    data object ModeratorPanel : DashboardRoutes()
}
