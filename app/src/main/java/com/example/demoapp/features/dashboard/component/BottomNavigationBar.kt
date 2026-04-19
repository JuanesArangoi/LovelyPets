package com.example.demoapp.features.dashboard.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.ShieldMoon
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.demoapp.features.dashboard.navigation.DashboardRoutes

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    destinations: List<Destination>,
    titleTopBar: (String) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Colores del tema
    val customGreenDark = Color(0xFF003913)
    val customGreenLight = Color(0xFFAFD8C0)

    LaunchedEffect(currentDestination) {
        val destination = destinations.find {
            it.route::class.qualifiedName == currentDestination?.route
        }
        if (destination != null) {
            titleTopBar(destination.label)
        }
    }

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White
    ) {
        destinations.forEach { destination ->
            val isSelected = currentDestination?.route == destination.route::class.qualifiedName

            NavigationBarItem(
                label = { Text(text = destination.label, color = if (isSelected) customGreenDark else Color.Gray) },
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label
                    )
                },
                selected = isSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = customGreenDark,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = customGreenLight
                )
            )
        }
    }
}

data class Destination(
    val route: DashboardRoutes,
    val label: String,
    val icon: ImageVector
)

@Composable
fun userDestinations(): List<Destination> = listOf(
    Destination(DashboardRoutes.PetFeed, "Inicio", Icons.Default.Home),
    Destination(DashboardRoutes.Map, "Mapa", Icons.Default.LocationOn), // MAPA AÑADIDO
    Destination(DashboardRoutes.CreatePet, "Publicar", Icons.Default.Add),
    Destination(DashboardRoutes.Profile, "Perfil", Icons.Default.AccountCircle)
)

@Composable
fun adminDestinations(): List<Destination> = listOf(
    Destination(DashboardRoutes.PetFeed, "Inicio", Icons.Default.Home),
    Destination(DashboardRoutes.Map, "Mapa", Icons.Default.LocationOn), // MAPA AÑADIDO
    Destination(DashboardRoutes.ModeratorPanel, "Moderación", Icons.Outlined.ShieldMoon),
    Destination(DashboardRoutes.Profile, "Perfil", Icons.Default.AccountCircle)
)
