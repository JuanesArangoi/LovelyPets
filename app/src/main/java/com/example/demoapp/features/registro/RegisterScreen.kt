package com.example.demoapp.features.register

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demoapp.R

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel() // Se crea o se obtiene el ViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = CenterVertically)
    ) {

        Image(
            painter = painterResource(R.mipmap.mascota),
            contentDescription = "Logo de la Aplicación"
        )

        Text(
            text = "Registro",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.fullName.value, // Estado del campo de nombre completo desde el ViewModel
            onValueChange = { viewModel.fullName.onChange(it) }, // Actualiza el estado en el ViewModel
            label = {
                Text(text = "Nombres completos")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Icono de persona"
                )
            },
            isError = viewModel.fullName.error != null, // Se muestra el borde rojo si hay error
            supportingText = viewModel.fullName.error?.let { error ->
                { Text(text = error) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.email.value, // Estado del campo de email desde el ViewModel
            onValueChange = { viewModel.email.onChange(it) }, // Actualiza el estado en el ViewModel
            label = {
                Text(text = "Correo electrónico")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Icono de email"
                )
            },
            isError = viewModel.email.error != null, // Se muestra el borde rojo si hay error
            supportingText = viewModel.email.error?.let { error ->
                { Text(text = error) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.password.value, // Estado del campo de password desde el ViewModel
            onValueChange = { viewModel.password.onChange(it) }, // Actualiza el estado en el ViewModel
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(text = "Contraseña")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Icono de contraseña"
                )
            },
            isError = viewModel.password.error != null, // Se muestra el borde rojo si hay error
            supportingText = viewModel.password.error?.let { error ->
                { Text(text = error) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.confirmPassword.value, // Estado del campo de confirmar password desde el ViewModel
            onValueChange = { viewModel.confirmPassword.onChange(it) }, // Actualiza el estado en el ViewModel
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(text = "Confirmar contraseña")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Icono de confirmar contraseña"
                )
            },
            isError = viewModel.confirmPassword.error != null, // Se muestra el borde rojo si hay error
            supportingText = viewModel.confirmPassword.error?.let { error ->
                { Text(text = error) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.phone.value, // Estado del campo de teléfono desde el ViewModel
            onValueChange = { viewModel.phone.onChange(it) }, // Actualiza el estado en el ViewModel
            label = {
                Text(text = "Teléfono")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Icono de teléfono"
                )
            },
            isError = viewModel.phone.error != null, // Se muestra el borde rojo si hay error
            supportingText = viewModel.phone.error?.let { error ->
                { Text(text = error) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.location.value, // Estado del campo de ubicación desde el ViewModel
            onValueChange = { viewModel.location.onChange(it) }, // Actualiza el estado en el ViewModel
            label = {
                Text(text = "Ubicación")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Icono de ubicación"
                )
            },
            isError = viewModel.location.error != null, // Se muestra el borde rojo si hay error
            supportingText = viewModel.location.error?.let { error ->
                { Text(text = error) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Button(
            onClick = {
                // Se imprime la información en el logcat por ahora
                Log.d("Register", "Nombre: ${viewModel.fullName.value}, Email: ${viewModel.email.value}, Teléfono: ${viewModel.phone.value}, Ubicación: ${viewModel.location.value}")
            },
            enabled = viewModel.isFormValid,
            content = {
                Text(text = "Registrarse")
            }
        )
    }
}