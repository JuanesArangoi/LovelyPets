package com.example.demoapp.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demoapp.R

/**
 * Pantalla de bienvenida de la aplicación LovelyPets.
 * Ofrece opciones para iniciar sesión o crear una cuenta nueva.
 */
@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,      // Función para navegar a la pantalla de Login
    onNavigateToRegister: () -> Unit    // Función para navegar a la pantalla de Registro
) {
    // Definimos los colores personalizados que se definieron en los mockups
    val buttonBackgroundColor = Color(0xFFAFD8C0)
    val buttonContentColor = Color(0xFF003913)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
    ) {
        // Logo de la aplicación
        Image(
            painter = painterResource(R.drawable.icono),
            contentDescription = "Logo de LovelyPets"
        )

        // Texto de bienvenida en negrita
        Text(
            text = "Bienvenid@ a LovelyPets3",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Red de adopción y protección de mascotas",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de navegación
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón de iniciar sesión
            Button(
                modifier = Modifier.size(width = 170.dp, height = 50.dp),
                onClick = onNavigateToLogin, // Navegar a Login
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBackgroundColor,
                    contentColor = buttonContentColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Icono de persona"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Iniciar Sesión")
            }

            // Botón de crear cuenta
            Button(
                modifier = Modifier.size(width = 170.dp, height = 50.dp),
                onClick = onNavigateToRegister, // Navegar a Registro
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBackgroundColor,
                    contentColor = buttonContentColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Icono de favorito"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Crear Cuenta")
            }
        }
    }
}
