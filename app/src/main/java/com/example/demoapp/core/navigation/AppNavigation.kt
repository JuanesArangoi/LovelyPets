package com.example.demoapp.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.demoapp.data.model.UserSession
import com.example.demoapp.domain.model.UserRole
import com.example.demoapp.features.dashboard.admin.AdminDashboardScreen
import com.example.demoapp.features.dashboard.user.UserDashboardScreen
import com.example.demoapp.features.home.HomeScreen
import com.example.demoapp.features.login.ChangePasswordScreen
import com.example.demoapp.features.login.LoginScreen
import com.example.demoapp.features.login.SendCodeScreen
import com.example.demoapp.features.login.VerifyCodeScreen
import com.example.demoapp.features.registro.RegisterScreen

/**
 * Composable raíz de navegación de la aplicación.
 * Observa el estado de sesión de DataStore y muestra el flujo correcto:
 *  - Loading  → indicador de carga
 *  - NotAuthenticated → flujo de autenticación (Home, Login, Register, …)
 *  - Authenticated    → flujo principal según el rol del usuario
 */
@Composable
fun AppNavigation(
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val sessionState by sessionViewModel.sessionState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        when (val state = sessionState) {
            is SessionState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is SessionState.NotAuthenticated -> {
                AuthNavigation(
                    onSessionStarted = { userId, role ->
                        sessionViewModel.login(userId, role)
                    }
                )
            }

            is SessionState.Authenticated -> {
                MainNavigation(
                    session = state.session,
                    onLogout = sessionViewModel::logout
                )
            }
        }
    }
}

/**
 * Flujo de navegación para usuarios NO autenticados.
 * Cubre: Home, Login, Register, SendCode, VerifyCode, ChangePassword.
 */
@Composable
private fun AuthNavigation(
    onSessionStarted: (userId: String, role: UserRole) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainRoutes.Home
    ) {
        // Pantalla de bienvenida
        composable<MainRoutes.Home> {
            HomeScreen(
                onNavigateToLogin = { navController.navigate(MainRoutes.Login) },
                onNavigateToRegister = { navController.navigate(MainRoutes.Register) }
            )
        }

        // Inicio de sesión – al hacer login exitoso, se guarda sesión y AppNavigation
        // cambia automáticamente a MainNavigation gracias al StateFlow de DataStore.
        composable<MainRoutes.Login> {
            LoginScreen(
                onSessionStarted = onSessionStarted,
                onForgotPasswordClick = { navController.navigate(MainRoutes.SendCode) },
                onRegisterClick = { navController.navigate(MainRoutes.Register) }
            )
        }

        // Registro de nuevo usuario
        composable<MainRoutes.Register> {
            RegisterScreen(
                onSessionStarted = onSessionStarted,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Recuperación de contraseña
        composable<MainRoutes.SendCode> {
            SendCodeScreen(
                onNavigateToVerifyCode = { navController.navigate(MainRoutes.VerifyCode) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<MainRoutes.VerifyCode> {
            VerifyCodeScreen(
                onNavigateToChangePassword = { navController.navigate(MainRoutes.ChangePassword) },
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
    }
}

/**
 * Flujo de navegación para usuarios autenticados.
 * Redirige al dashboard correspondiente según el rol.
 */
@Composable
private fun MainNavigation(
    session: UserSession,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    // Pantalla inicial según el rol
    val startDestination: Any = when (session.role) {
        UserRole.MODERADOR -> MainRoutes.AdminDashboard
        UserRole.USUARIO   -> MainRoutes.UserDashboard
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<MainRoutes.UserDashboard> {
            UserDashboardScreen(onLogout = onLogout)
        }

        composable<MainRoutes.AdminDashboard> {
            AdminDashboardScreen(onLogout = onLogout)
        }
    }
}
