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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demoapp.R

/**
 * Pantalla para enviar código de recuperación de contraseña.
 * Usa hiltViewModel() para inyectar SendCodeViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendCodeScreen(
    viewModel: SendCodeViewModel = hiltViewModel(),
    onNavigateToVerifyCode: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    // Observar resultado del envío
    LaunchedEffect(viewModel.sendResult) {
        viewModel.sendResult?.let { success ->
            if (success) {
                viewModel.resetResult()
                onNavigateToVerifyCode()
            }
        }
    }

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

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Email") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Icono de email"
                    )
                },
                value = viewModel.email.value,
                isError = viewModel.email.error != null,
                supportingText = viewModel.email.error?.let { error -> { Text(text = error) } },
                onValueChange = { viewModel.email.onChange(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Button(
                onClick = { viewModel.sendCode() },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.email.isValid
            ) {
                Text("Enviar código")
            }
        }
    }
}
