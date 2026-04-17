package com.example.demoapp.features.dashboard.admin

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.example.demoapp.R
import com.example.demoapp.features.dashboard.component.BottomNavigationBar
import com.example.demoapp.features.dashboard.component.LovelyPetsTopAppBar
import com.example.demoapp.features.dashboard.component.adminDestinations
import com.example.demoapp.features.dashboard.navigation.UserNavigation

@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    var title by remember { mutableStateOf("") }
    val defaultTitle = stringResource(R.string.dashboard_title)

    if (title.isEmpty()) {
        title = defaultTitle
    }

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
                destinations = adminDestinations(),
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
