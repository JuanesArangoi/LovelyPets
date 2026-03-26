package com.example.demoapp.features.dashboard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.demoapp.features.moderator.ModeratorPanelScreen
import com.example.demoapp.features.pets.create.CreatePetScreen
import com.example.demoapp.features.pets.detail.PetDetailScreen
import com.example.demoapp.features.pets.edit.EditPetScreen
import com.example.demoapp.features.pets.list.PetListScreen
import com.example.demoapp.features.profile.UserProfileScreen

/**
 * NavHost interno para la navegación dentro del dashboard.
 * Gestiona las pantallas accesibles desde la barra de navegación inferior.
 * Este NavHost es diferente al principal de la aplicación (AppNavigation).
 */
@Composable
fun UserNavigation(
    navController: NavHostController,
    padding: PaddingValues,
    showModeratorPanel: Boolean = false
) {
    NavHost(
        navController = navController,
        startDestination = DashboardRoutes.PetFeed
    ) {
        // === Feed de mascotas (pantalla principal) ===
        composable<DashboardRoutes.PetFeed> {
            PetListScreen(
                paddingValues = padding,
                onNavigateToPetDetail = { petId ->
                    navController.navigate(DashboardRoutes.PetDetail(petId))
                },
                onNavigateToCreatePet = {
                    navController.navigate(DashboardRoutes.CreatePet)
                }
            )
        }

        // === Perfil del usuario ===
        composable<DashboardRoutes.Profile> {
            UserProfileScreen(
                paddingValues = padding,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAccountDeleted = {
                    // La navegación de vuelta a Home se maneja desde el dashboard padre
                }
            )
        }

        // === Detalle de publicación ===
        composable<DashboardRoutes.PetDetail> {
            val args = it.toRoute<DashboardRoutes.PetDetail>()
            PetDetailScreen(
                petId = args.petId,
                paddingValues = padding,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToEdit = { petId ->
                    navController.navigate(DashboardRoutes.EditPet(petId))
                }
            )
        }

        // === Crear publicación ===
        composable<DashboardRoutes.CreatePet> {
            CreatePetScreen(
                paddingValues = padding,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // === Editar publicación ===
        composable<DashboardRoutes.EditPet> {
            val args = it.toRoute<DashboardRoutes.EditPet>()
            EditPetScreen(
                petId = args.petId,
                paddingValues = padding,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // === Panel de moderador (solo visible para moderadores) ===
        if (showModeratorPanel) {
            composable<DashboardRoutes.ModeratorPanel> {
                ModeratorPanelScreen(
                    paddingValues = padding,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
