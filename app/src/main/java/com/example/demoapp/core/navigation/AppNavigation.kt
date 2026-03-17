package com.example.demoapp.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.demoapp.features.home.HomeScreen
import com.example.demoapp.features.login.ChangePasswordScreen
import com.example.demoapp.features.login.LoginScreen
import com.example.demoapp.features.login.SendCodeScreen
import com.example.demoapp.features.login.VerifyCodeScreen
import com.example.demoapp.features.moderator.ModeratorPanelScreen
import com.example.demoapp.features.pets.create.CreatePetScreen
import com.example.demoapp.features.pets.detail.PetDetailScreen
import com.example.demoapp.features.pets.edit.EditPetScreen
import com.example.demoapp.features.pets.list.PetListScreen
import com.example.demoapp.features.profile.UserProfileScreen
import com.example.demoapp.features.registro.RegisterScreen

/**
 * Composable principal de navegación de la aplicación.
 * Define el NavHost con todas las rutas y conecta las pantallas entre sí.
 */
@Composable
fun AppNavigation() {
    // Estado de navegación, controla la navegación entre pantallas
    val navController = rememberNavController()

    // Surface que ocupa toda la pantalla y se adapta al tema
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = MainRoutes.Home  // Pantalla inicial: Bienvenida
        ) {

            // ===== PANTALLA DE BIENVENIDA =====
            composable<MainRoutes.Home> {
                HomeScreen(
                    onNavigateToLogin = {
                        navController.navigate(MainRoutes.Login)
                    },
                    onNavigateToRegister = {
                        navController.navigate(MainRoutes.Register)
                    }
                )
            }

            // ===== PANTALLA DE LOGIN =====
            composable<MainRoutes.Login> {
                LoginScreen(
                    onNavigateToPetList = {
                        // Navegar al feed y limpiar el back stack para que no pueda volver al login
                        navController.navigate(MainRoutes.PetList) {
                            popUpTo(MainRoutes.Home) { inclusive = true }
                        }
                    },
                    onForgotPasswordClick = {
                        navController.navigate(MainRoutes.SendCode)
                    },
                    onRegisterClick = {
                        navController.navigate(MainRoutes.Register)
                    }
                )
            }

            // ===== PANTALLA DE REGISTRO =====
            composable<MainRoutes.Register> {
                RegisterScreen(
                    onRegisterSuccess = {
                        // Navegar al feed y limpiar el back stack
                        navController.navigate(MainRoutes.PetList) {
                            popUpTo(MainRoutes.Home) { inclusive = true }
                        }
                    }
                )
            }

            // ===== RECUPERACIÓN DE CONTRASEÑA: ENVIAR CÓDIGO =====
            composable<MainRoutes.SendCode> {
                SendCodeScreen(
                    onNavigateToVerifyCode = {
                        navController.navigate(MainRoutes.VerifyCode)
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // ===== RECUPERACIÓN DE CONTRASEÑA: VERIFICAR CÓDIGO =====
            composable<MainRoutes.VerifyCode> {
                VerifyCodeScreen(
                    onNavigateToChangePassword = {
                        navController.navigate(MainRoutes.ChangePassword)
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // ===== CAMBIAR CONTRASEÑA =====
            composable<MainRoutes.ChangePassword> {
                ChangePasswordScreen(
                    onNavigateToLogin = {
                        // Volver al login limpiando el stack de recuperación
                        navController.navigate(MainRoutes.Login) {
                            popUpTo(MainRoutes.Home)
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // ===== FEED DE PUBLICACIONES DE MASCOTAS =====
            composable<MainRoutes.PetList> {
                PetListScreen(
                    onNavigateToPetDetail = { petId ->
                        navController.navigate(MainRoutes.PetDetail(petId))
                    },
                    onNavigateToCreatePet = {
                        navController.navigate(MainRoutes.CreatePet)
                    },
                    onNavigateToProfile = {
                        navController.navigate(MainRoutes.UserProfile)
                    },
                    onNavigateToModeratorPanel = {
                        navController.navigate(MainRoutes.ModeratorPanel)
                    },
                    onLogout = {
                        // Volver a Home limpiando todo el back stack
                        navController.navigate(MainRoutes.Home) {
                            popUpTo(MainRoutes.Home) { inclusive = true }
                        }
                    }
                )
            }

            // ===== DETALLE DE PUBLICACIÓN DE MASCOTA =====
            composable<MainRoutes.PetDetail> {
                // Obtener el ID de la mascota desde los argumentos de la ruta
                val args = it.toRoute<MainRoutes.PetDetail>()
                PetDetailScreen(
                    petId = args.petId,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToEdit = { petId ->
                        navController.navigate(MainRoutes.EditPet(petId))
                    }
                )
            }

            // ===== CREAR NUEVA PUBLICACIÓN =====
            composable<MainRoutes.CreatePet> {
                CreatePetScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // ===== EDITAR PUBLICACIÓN EXISTENTE =====
            composable<MainRoutes.EditPet> {
                val args = it.toRoute<MainRoutes.EditPet>()
                EditPetScreen(
                    petId = args.petId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // ===== PERFIL DEL USUARIO =====
            composable<MainRoutes.UserProfile> {
                UserProfileScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onAccountDeleted = {
                        // Si se elimina la cuenta, volver a Home
                        navController.navigate(MainRoutes.Home) {
                            popUpTo(MainRoutes.Home) { inclusive = true }
                        }
                    }
                )
            }

            // ===== PANEL DE MODERADOR =====
            composable<MainRoutes.ModeratorPanel> {
                ModeratorPanelScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
