package com.example.demoapp.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demoapp.R
import com.example.demoapp.domain.model.UserRole

/**
 * Pantalla de inicio de sesión.
 * Usa hiltViewModel() para inyectar el LoginViewModel via Hilt.
 * Navega al dashboard de usuario o moderador según el rol.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToUserDashboard: () -> Unit = {},
    onNavigateToAdminDashboard: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val currentUser by viewModel.currentUser.collectAsState()

    // Efecto para manejar el resultado del login
    LaunchedEffect(viewModel.loginResult) {
        viewModel.loginResult?.let { success ->
            if (success) {
                viewModel.resetForm()
                // Navegar según el rol del usuario
                if (currentUser?.role == UserRole.MODERADOR) {
                    onNavigateToAdminDashboard()
                } else {
                    onNavigateToUserDashboard()
                }
            } else {
                snackbarHostState.showSnackbar("Credenciales incorrectas")
                viewModel.resetLoginResult()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                text = "Inicio de Sesión",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.email.value,
                onValueChange = { viewModel.email.onChange(it) },
                label = { Text(text = "Email") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email")
                },
                isError = viewModel.email.error != null,
                supportingText = viewModel.email.error?.let { error -> { Text(text = error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.password.value,
                onValueChange = { viewModel.password.onChange(it) },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(text = "Contraseña") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Contraseña")
                },
                isError = viewModel.password.error != null,
                supportingText = viewModel.password.error?.let { error -> { Text(text = error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Button(
                onClick = { viewModel.login() },
                enabled = viewModel.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Iniciar sesión")
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Iniciar Sesión")
            }

            Text(
                text = "¿Olvidaste tu contraseña?",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { onForgotPasswordClick() }
            )

            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { onRegisterClick() }
            )
        }
    }
}