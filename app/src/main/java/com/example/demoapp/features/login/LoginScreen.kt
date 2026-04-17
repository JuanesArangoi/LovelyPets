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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

/**
 * Pantalla de inicio de sesión.
 * Tras un login exitoso, llama a onSessionStarted(userId, role) para que
 * AppNavigation persista la sesión en DataStore y cambie el flujo de navegación.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onSessionStarted: (userId: String, role: UserRole) -> Unit = { _, _ -> },
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val loginResult by viewModel.loginResult.collectAsState()

    // Reacciona al resultado del login
    LaunchedEffect(loginResult) {
        loginResult?.let { success ->
            if (!success) {
                snackbarHostState.showSnackbar(
                    message = "Credenciales incorrectas"
                )
                viewModel.resetLoginResult()
            }
            // Si fue exitoso, el resultado lo maneja el scope de la corrutina que llama a login()
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
                contentDescription = stringResource(R.string.app_logo_description)
            )

            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.email.value,
                onValueChange = { viewModel.email.onChange(it) },
                label = { Text(text = stringResource(R.string.login_email_label)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = stringResource(R.string.login_email_label)
                    )
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
                label = { Text(text = stringResource(R.string.login_password_label)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(R.string.login_password_label)
                    )
                },
                isError = viewModel.password.error != null,
                supportingText = viewModel.password.error?.let { error -> { Text(text = error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        val result = viewModel.login()
                        if (result != null) {
                            viewModel.resetForm()
                            onSessionStarted(result.first, result.second)
                        }
                    }
                },
                enabled = viewModel.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.login_icon_description)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = stringResource(R.string.login_button))
            }

            Text(
                text = stringResource(R.string.login_forgot_password),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { onForgotPasswordClick() }
            )

            Text(
                text = stringResource(R.string.login_register_link),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { onRegisterClick() }
            )
        }
    }
}