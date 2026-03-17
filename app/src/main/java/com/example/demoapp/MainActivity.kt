package com.example.demoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.demoapp.core.navigation.AppNavigation
import com.example.demoapp.ui.theme.DemoAppTheme

/**
 * Actividad principal de la aplicación LovelyPets.
 * Usa AppNavigation para manejar toda la navegación entre pantallas.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoAppTheme {
                // La navegación de la aplicación se maneja desde AppNavigation
                AppNavigation()
            }
        }
    }
}
