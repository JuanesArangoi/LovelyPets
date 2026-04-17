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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.changeResult) {
        viewModel.changeResult?.let { success ->
            if (success) {
                snackbarHostState.showSnackbar("✅")
                viewModel.resetResult()
                onNavigateToLogin()
            } else {
                viewModel.resetResult()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.change_password_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
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
                contentDescription = stringResource(R.string.app_logo_description)
            )

            Text(
                text = stringResource(R.string.change_password_title),
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.newPassword.value,
                onValueChange = { viewModel.newPassword.onChange(it) },
                label = { Text(stringResource(R.string.change_password_new_label)) },
                visualTransformation = PasswordVisualTransformation(),
                isError = viewModel.newPassword.error != null,
                supportingText = viewModel.newPassword.error?.let { error -> { Text(text = error) } }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.confirmPassword.value,
                onValueChange = { viewModel.confirmPassword.onChange(it) },
                label = { Text(stringResource(R.string.change_password_confirm_label)) },
                visualTransformation = PasswordVisualTransformation(),
                isError = viewModel.confirmPassword.error != null,
                supportingText = viewModel.confirmPassword.error?.let { error -> { Text(text = error) } }
            )

            Button(
                onClick = { viewModel.changePassword() },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.isFormValid
            ) {
                Text(stringResource(R.string.change_password_button))
            }
        }
    }
}
