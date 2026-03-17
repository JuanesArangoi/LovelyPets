package com.example.demoapp.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.demoapp.R

/**
 * Pantalla para verificar el código de recuperación de contraseña.
 * El usuario ingresa el código recibido por email.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyCodeScreen(
    onNavigateToChangePassword: () -> Unit = {},  // Navegar a cambiar contraseña
    onNavigateBack: () -> Unit = {}               // Volver atrás
) {
    var code by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verificar Código") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = CenterVertically)
        ) {
            // Logo
            Image(
                painter = painterResource(R.mipmap.mascota),
                contentDescription = "Logo de la Aplicación"
            )

            Text(
                text = "Verificar Código",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Ingresa el código que recibiste en tu correo electrónico",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Campo de código
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = code,
                onValueChange = { code = it },
                label = { Text("Código de verificación") }
            )

            // Botón de validar código
            Button(
                onClick = { onNavigateToChangePassword() },
                modifier = Modifier.fillMaxWidth(),
                enabled = code.isNotEmpty()
            ) {
                Text("Validar Código")
            }
        }
    }
}
