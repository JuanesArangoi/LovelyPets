package com.example.demoapp.features.dashboard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.demoapp.features.map.MapScreen
import com.example.demoapp.features.moderator.ModeratorPanelScreen
import com.example.demoapp.features.notifications.NotificationsScreen
import com.example.demoapp.features.pets.create.CreatePetScreen
import com.example.demoapp.features.pets.detail.PetDetailScreen
import com.example.demoapp.features.pets.edit.EditPetScreen
import com.example.demoapp.features.pets.list.PetListScreen
import com.example.demoapp.features.profile.UserProfileScreen
import com.example.demoapp.features.reports.AdminReportsScreen
import com.example.demoapp.features.achievements.AchievementsScreen

@Composable
fun UserNavigation(
    navController: NavHostController,
    padding: PaddingValues,
    showModeratorPanel: Boolean = false,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = DashboardRoutes.PetFeed
    ) {
        composable<DashboardRoutes.PetFeed> {
            PetListScreen(
                paddingValues = padding, // AÑADIDO: Pasamos el padding para que no se corte al final
                onNavigateToPetDetail = { petId ->
                    navController.navigate(DashboardRoutes.PetDetail(petId))
                },
                onNavigateToCreatePet = {
                    navController.navigate(DashboardRoutes.CreatePet)
                }
            )
        }

        composable<DashboardRoutes.Map> {
            MapScreen(paddingValues = padding)
        }

        composable<DashboardRoutes.Notifications> {
            NotificationsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<DashboardRoutes.Profile> {
            UserProfileScreen(
                paddingValues = padding,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAccountDeleted = {
                    onLogout()
                },
                onLogout = onLogout,
                onNavigateToEdit = { petId ->
                    navController.navigate(DashboardRoutes.EditPet(petId))
                },
                onNavigateToAchievements = {
                    navController.navigate(DashboardRoutes.Achievements)
                }
            )
        }

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

        composable<DashboardRoutes.CreatePet> {
            CreatePetScreen(
                paddingValues = padding,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<DashboardRoutes.Achievements> {
            AchievementsScreen(
                paddingValues = padding,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

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

        if (showModeratorPanel) {
            composable<DashboardRoutes.ModeratorPanel> {
                ModeratorPanelScreen(
                    paddingValues = padding,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<DashboardRoutes.AdminReports> {
                AdminReportsScreen(
                    paddingValues = padding
                )
            }
        }
    }
}
