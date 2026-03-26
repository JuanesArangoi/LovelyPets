package com.example.demoapp.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.demoapp.features.dashboard.admin.AdminDashboardScreen
import com.example.demoapp.features.dashboard.user.UserDashboardScreen
import com.example.demoapp.features.home.HomeScreen
import com.example.demoapp.features.login.ChangePasswordScreen
import com.example.demoapp.features.login.LoginScreen
import com.example.demoapp.features.login.SendCodeScreen
import com.example.demoapp.features.login.VerifyCodeScreen
import com.example.demoapp.features.registro.RegisterScreen

/**
 * Composable principal de navegación de la aplicación.
 * Define el NavHost con las rutas principales.
 * Las pantallas internas (feed, detalle, perfil, etc.) se gestionan
 * dentro de los dashboards con navegación anidada.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = MainRoutes.Home
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
                    onNavigateToUserDashboard = {
                        navController.navigate(MainRoutes.UserDashboard) {
                            popUpTo(MainRoutes.Home) { inclusive = true }
                        }
                    },
                    onNavigateToAdminDashboard = {
                        navController.navigate(MainRoutes.AdminDashboard) {
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
                        navController.navigate(MainRoutes.UserDashboard) {
                            popUpTo(MainRoutes.Home) { inclusive = true }
                        }
                    }
                )
            }

            // ===== RECUPERACIÓN DE CONTRASEÑA =====
            composable<MainRoutes.SendCode> {
                SendCodeScreen(
                    onNavigateToVerifyCode = {
                        navController.navigate(MainRoutes.VerifyCode)
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<MainRoutes.VerifyCode> {
                VerifyCodeScreen(
                    onNavigateToChangePassword = {
                        navController.navigate(MainRoutes.ChangePassword)
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<MainRoutes.ChangePassword> {
                ChangePasswordScreen(
                    onNavigateToLogin = {
                        navController.navigate(MainRoutes.Login) {
                            popUpTo(MainRoutes.Home)
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // ===== DASHBOARD DEL USUARIO (con BottomNavigationBar) =====
            composable<MainRoutes.UserDashboard> {
                UserDashboardScreen(
                    onLogout = {
                        navController.navigate(MainRoutes.Home) {
                            popUpTo(MainRoutes.Home) { inclusive = true }
                        }
                    }
                )
            }

            // ===== DASHBOARD DEL MODERADOR (con BottomNavigationBar) =====
            composable<MainRoutes.AdminDashboard> {
                AdminDashboardScreen(
                    onLogout = {
                        navController.navigate(MainRoutes.Home) {
                            popUpTo(MainRoutes.Home) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
