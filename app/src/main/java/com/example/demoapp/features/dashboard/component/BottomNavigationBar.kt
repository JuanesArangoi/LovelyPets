package com.example.demoapp.features.dashboard.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.demoapp.R
import com.example.demoapp.features.dashboard.navigation.DashboardRoutes

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    destinations: List<Destination>,
    titleTopBar: (String) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    LaunchedEffect(currentDestination) {
        val destination = destinations.find {
            it.route::class.qualifiedName == currentDestination?.route
        }
        if (destination != null) {
            titleTopBar(destination.label)
        }
    }

    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        destinations.forEach { destination ->
            val isSelected = currentDestination?.route == destination.route::class.qualifiedName

            NavigationBarItem(
                label = { Text(text = destination.label) },
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
                selected = isSelected
            )
        }
    }
}

data class Destination(
    val route: DashboardRoutes,
    val label: String,
    val icon: ImageVector
)

/**
 * Las listas de destinos se crean como @Composable para poder usar stringResource()
 */
@Composable
fun userDestinations(): List<Destination> = listOf(
    Destination(DashboardRoutes.PetFeed, stringResource(R.string.nav_feed), Icons.Default.Home),
    Destination(DashboardRoutes.CreatePet, stringResource(R.string.nav_create), Icons.Default.Add),
    Destination(DashboardRoutes.Profile, stringResource(R.string.nav_profile), Icons.Default.AccountCircle)
)

@Composable
fun adminDestinations(): List<Destination> = listOf(
    Destination(DashboardRoutes.PetFeed, stringResource(R.string.nav_feed), Icons.Default.Home),
    Destination(DashboardRoutes.ModeratorPanel, stringResource(R.string.nav_moderator), Icons.Default.ShieldMoon),
    Destination(DashboardRoutes.Profile, stringResource(R.string.nav_profile), Icons.Default.AccountCircle)
)
