package com.example.demoapp.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.demoapp.R

/**
 * Pantalla para enviar código de recuperación de contraseña.
 * El usuario ingresa su email y se le envía un código de verificación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendCodeScreen(
    onNavigateToVerifyCode: () -> Unit = {},  // Navegar a verificar código
    onNavigateBack: () -> Unit = {}           // Volver atrás
) {
    var email by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recuperar Contraseña") },
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
                text = "Recuperar Contraseña",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Ingresa tu email y te enviaremos un código de verificación",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Campo de email
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Email") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Icono de email"
                    )
                },
                value = email,
                isError = errorEmail.isNotEmpty(),
                supportingText = {
                    if (errorEmail.isNotEmpty()) {
                        Text(text = errorEmail)
                    }
                },
                onValueChange = {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                        errorEmail = "El email no es correcto"
                    } else {
                        errorEmail = ""
                    }
                    email = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            // Botón de enviar código
            Button(
                onClick = { onNavigateToVerifyCode() },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotEmpty() && errorEmail.isEmpty()
            ) {
                Text("Enviar código")
            }
        }
    }
}
