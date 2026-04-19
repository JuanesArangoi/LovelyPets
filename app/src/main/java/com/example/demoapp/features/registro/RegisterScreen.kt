package com.example.demoapp.features.registro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demoapp.R
import com.example.demoapp.domain.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onSessionStarted: (userId: String, role: UserRole) -> Unit = { _, _ -> },
    onNavigateBack: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Definición de colores
    val customGreenDark = Color(0xFF003913)
    val customGreenLight = Color(0xFFAFD8C0)

    LaunchedEffect(viewModel.registerResult) {
        viewModel.registerResult?.let { (userId, role) ->
            viewModel.resetForm()
            onSessionStarted(userId, role)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.register_title), color = customGreenDark, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = customGreenLight),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description),
                            tint = customGreenDark
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.mascota),
                contentDescription = stringResource(R.string.app_logo_description),
                modifier = Modifier.size(120.dp)
            )

            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = customGreenDark,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = customGreenDark,
                cursorColor = customGreenDark
            )

            // Campo: Nombre completo
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.fullName.value,
                onValueChange = { viewModel.fullName.onChange(it) },
                label = { Text(text = stringResource(R.string.register_fullname_label)) },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = customGreenDark) },
                colors = textFieldColors,
                isError = viewModel.fullName.error != null,
                supportingText = viewModel.fullName.error?.let { error -> { Text(text = error) } }
            )

            // Campo: Correo electrónico
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.email.value,
                onValueChange = { viewModel.email.onChange(it) },
                label = { Text(text = stringResource(R.string.login_email_label)) },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = customGreenDark) },
                colors = textFieldColors,
                isError = viewModel.email.error != null,
                supportingText = viewModel.email.error?.let { error -> { Text(text = error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            // Campo: Contraseña
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.password.value,
                onValueChange = { viewModel.password.onChange(it) },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(text = stringResource(R.string.login_password_label)) },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = customGreenDark) },
                colors = textFieldColors,
                isError = viewModel.password.error != null,
                supportingText = viewModel.password.error?.let { error -> { Text(text = error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            // Campo: Confirmar Contraseña
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.confirmPassword.value,
                onValueChange = { viewModel.confirmPassword.onChange(it) },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(text = stringResource(R.string.register_confirm_password_label)) },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = customGreenDark) },
                colors = textFieldColors,
                isError = viewModel.confirmPassword.error != null,
                supportingText = viewModel.confirmPassword.error?.let { error -> { Text(text = error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            // Campo: Teléfono
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.phone.value,
                onValueChange = { viewModel.phone.onChange(it) },
                label = { Text(text = stringResource(R.string.register_phone_label)) },
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = customGreenDark) },
                colors = textFieldColors,
                isError = viewModel.phone.error != null,
                supportingText = viewModel.phone.error?.let { error -> { Text(text = error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            // Campo: Ciudad
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.location.value,
                onValueChange = { viewModel.location.onChange(it) },
                label = { Text(text = stringResource(R.string.register_city_label)) },
                leadingIcon = { Icon(Icons.Default.Place, null, tint = customGreenDark) },
                colors = textFieldColors,
                isError = viewModel.location.error != null,
                supportingText = viewModel.location.error?.let { error -> { Text(text = error) } }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.register() },
                enabled = viewModel.isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = customGreenLight,
                    contentColor = customGreenDark
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.register_button),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
