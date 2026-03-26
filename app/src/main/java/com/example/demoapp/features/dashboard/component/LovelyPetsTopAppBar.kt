package com.example.demoapp.features.dashboard.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

/**
 * Barra superior reutilizable para el dashboard de LovelyPets.
 * Muestra el título dinámico y un botón de cierre de sesión.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LovelyPetsTopAppBar(
    title: String,
    logout: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            // Botón de cierre de sesión
            IconButton(onClick = logout) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión"
                )
            }
        }
    )
}
