package com.example.demoapp.features.dashboard.admin

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.demoapp.features.dashboard.component.BottomNavigationBar
import com.example.demoapp.features.dashboard.component.LovelyPetsTopAppBar
import com.example.demoapp.features.dashboard.component.adminDestinations
import com.example.demoapp.features.dashboard.navigation.UserNavigation

/**
 * Pantalla principal del dashboard para moderadores.
 * Similar al UserDashboardScreen pero con el tab "Moderar" en lugar de "Crear".
 */
@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit
) {
    // Estado de navegación interna del dashboard
    val navController = rememberNavController()
    var title by remember { mutableStateOf("LovelyPets - Moderador 🛡️") }

    Scaffold(
        topBar = {
            LovelyPetsTopAppBar(
                title = title,
                logout = onLogout
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                destinations = adminDestinations,
                titleTopBar = { title = it }
            )
        }
    ) { padding ->
        UserNavigation(
            navController = navController,
            padding = padding,
            showModeratorPanel = true
        )
    }
}
