package com.example.demoapp.features.registro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
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

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onRegisterSuccess: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val customGreenDark = Color(0xFF003913)
    val customGreenLight = Color(0xFFAFD8C0)

    LaunchedEffect(viewModel.registerResult) {
        viewModel.registerResult?.let { success ->
            if (success) {
                viewModel.resetForm()
                onRegisterSuccess()
            } else {
                snackbarHostState.showSnackbar("El email ya está registrado")
                viewModel.resetRegisterResult()
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
                .padding(30.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = CenterVertically)
        ) {
            Image(
                painter = painterResource(R.drawable.pet),
                contentDescription = "Logo"
            )

            Text(
                text = "Registro",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = customGreenDark
            )

            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = customGreenDark,
                focusedLabelColor = customGreenDark,
                focusedLeadingIconColor = customGreenDark,
                cursorColor = customGreenDark,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.fullName.value,
                onValueChange = { viewModel.fullName.onChange(it) },
                label = { Text("Nombres completos") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                colors = textFieldColors
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.email.value,
                onValueChange = { viewModel.email.onChange(it) },
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                colors = textFieldColors
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.password.value,
                onValueChange = { viewModel.password.onChange(it) },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                colors = textFieldColors
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.confirmPassword.value,
                onValueChange = { viewModel.confirmPassword.onChange(it) },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text("Confirmar contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                colors = textFieldColors
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.phone.value,
                onValueChange = { viewModel.phone.onChange(it) },
                label = { Text("Teléfono") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                colors = textFieldColors
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.location.value,
                onValueChange = { viewModel.location.onChange(it) },
                label = { Text("Ciudad") },
                leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                colors = textFieldColors
            )

            Button(
                onClick = { viewModel.register() },
                enabled = viewModel.isFormValid,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = customGreenLight,
                    contentColor = customGreenDark
                )
            ) {
                Text(text = "Registrarse", fontWeight = FontWeight.Bold)
            }
        }
    }
}
