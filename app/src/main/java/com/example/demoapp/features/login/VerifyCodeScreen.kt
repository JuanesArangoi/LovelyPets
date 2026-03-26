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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demoapp.R

/**
 * Pantalla para verificar el código de recuperación de contraseña.
 * Usa hiltViewModel() para inyectar VerifyCodeViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyCodeScreen(
    viewModel: VerifyCodeViewModel = hiltViewModel(),
    onNavigateToChangePassword: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    // Observar resultado de la verificación
    LaunchedEffect(viewModel.verifyResult) {
        viewModel.verifyResult?.let { success ->
            if (success) {
                viewModel.resetResult()
                onNavigateToChangePassword()
            }
        }
    }

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

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.code.value,
                onValueChange = { viewModel.code.onChange(it) },
                label = { Text("Código de verificación") },
                isError = viewModel.code.error != null,
                supportingText = viewModel.code.error?.let { error -> { Text(text = error) } }
            )

            Button(
                onClick = { viewModel.verifyCode() },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.code.isValid
            ) {
                Text("Validar Código")
            }
        }
    }
}
