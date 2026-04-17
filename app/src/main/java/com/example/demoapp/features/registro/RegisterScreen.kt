package com.example.demoapp.features.registro

import android.util.Log
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demoapp.R
import com.example.demoapp.domain.model.UserRole

/**
 * Pantalla de registro de nuevo usuario.
 * Tras el registro exitoso llama a onSessionStarted(userId, role) para que
 * AppNavigation persista la sesión y cambie el flujo de navegación automáticamente.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onSessionStarted: (userId: String, role: UserRole) -> Unit = { _, _ -> },
    onNavigateBack: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.registerResult) {
        viewModel.registerResult?.let { (userId, role) ->
            viewModel.resetForm()
            onSessionStarted(userId, role)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.register_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
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
                .padding(innerPadding)
                .padding(30.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = CenterVertically)
        ) {
            Image(
                painter = painterResource(R.mipmap.mascota),
                contentDescription = stringResource(R.string.app_logo_description)
            )

            // Campo: Nombre completo
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.fullName.value,
                onValueChange = { viewModel.fullName.onChange(it) },
                label = { Text(text = stringResource(R.string.register_fullname_label)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = stringResource(R.string.register_fullname_label))
                },
                isError = viewModel.fullName.error != null,
                supportingText = viewModel.fullName.error?.let { error -> { Text(text = error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            // Campo: Correo electrónico
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.email.value,
                onValueChange = { viewModel.email.onChange(it) },
                label = { Text(text = stringResource(R.string.login_email_label)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = stringResource(R.string.login_email_label))
                },
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
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = stringResource(R.string.login_password_label))
                },
                isError = viewModel.password.error != null,
                supportingText = viewModel.password.error?.let { error -> { Text(text = error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            // Campo: Confirmar contraseña
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.confirmPassword.value,
                onValueChange = { viewModel.confirmPassword.onChange(it) },
                visualTransformation = PasswordVisualTransformation(),
                label = { Text(text = stringResource(R.string.register_confirm_password_label)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = stringResource(R.string.register_confirm_password_label))
                },
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
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = stringResource(R.string.register_phone_label))
                },
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
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Place, contentDescription = stringResource(R.string.register_city_label))
                },
                isError = viewModel.location.error != null,
                supportingText = viewModel.location.error?.let { error -> { Text(text = error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Button(
                onClick = { viewModel.register() },
                enabled = viewModel.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.register_button))
            }
        }
    }
}