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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demoapp.R
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToPetList: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val customGreenDark = Color(0xFF003913)
    val customGreenLight = Color(0xFFAFD8C0)

    LaunchedEffect(viewModel.loginResult) {
        viewModel.loginResult?.let { success ->
            if (success) {
                viewModel.resetForm()
                onNavigateToPetList()
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
                painter = painterResource(R.drawable.icono),
                contentDescription = "Logo"
            )

            Text(
                text = "Inicio de Sesión",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = customGreenDark
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.email.value,
                onValueChange = { viewModel.email.onChange(it) },
                label = { Text(text = "Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                isError = viewModel.email.error != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = customGreenDark,
                    focusedLabelColor = customGreenDark,
                    focusedLeadingIconColor = customGreenDark,
                    cursorColor = customGreenDark,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.password.value,
                onValueChange = { viewModel.password.onChange(it) },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(text = "Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                isError = viewModel.password.error != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = customGreenDark,
                    focusedLabelColor = customGreenDark,
                    focusedLeadingIconColor = customGreenDark,
                    cursorColor = customGreenDark,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Button(
                onClick = { viewModel.login() },
                enabled = viewModel.isFormValid,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = customGreenLight,
                    contentColor = customGreenDark
                )
            ) {
                Text(text = "Iniciar Sesión", fontWeight = FontWeight.Bold)
            }

            Text(
                text = "¿Olvidaste tu contraseña?",
                color = customGreenDark,
                modifier = Modifier.clickable { onForgotPasswordClick() }
            )

            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = customGreenDark,
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }
    }
}
