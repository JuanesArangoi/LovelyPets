package com.example.demoapp.features.dashboard.user

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.demoapp.features.dashboard.component.BottomNavigationBar
import com.example.demoapp.features.dashboard.component.LovelyPetsTopAppBar
import com.example.demoapp.features.dashboard.component.userDestinations
import com.example.demoapp.features.dashboard.navigation.UserNavigation

/**
 * Pantalla principal del dashboard para usuarios normales.
 * Contiene el Scaffold con TopAppBar, BottomNavigationBar y el NavHost interno.
 */
@Composable
fun UserDashboardScreen(
    onLogout: () -> Unit
) {
    // Estado de navegación interna del dashboard
    val navController = rememberNavController()
    var title by remember { mutableStateOf("LovelyPets 🐾") }

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
                destinations = userDestinations,
                titleTopBar = { title = it }
            )
        }
    ) { padding ->
        UserNavigation(
            navController = navController,
            padding = padding,
            showModeratorPanel = false
        )
    }
}
