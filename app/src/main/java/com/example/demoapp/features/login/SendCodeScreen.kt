package com.example.demoapp.features.login


import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.demoapp.R

@Composable
fun SendCodeScreen() {
    var email by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(30.dp),
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


        OutlinedTextField(
            label = {
                Text(
                    text = "Email"
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Icono de email"
                )
            },
            value = email,

            isError = errorEmail.isNotEmpty(),
            supportingText = {
                if (errorEmail.isNotEmpty()){
                    Text (
                        text = errorEmail
                    )
                }
            },

            onValueChange = {

                if( !Patterns.EMAIL_ADDRESS.matcher(it).matches()){
                    errorEmail = "El email no es correcto"
                } else{
                    errorEmail = ""
                }
                email = it
            }
        )

        Button(
            onClick = {
                // Aquí iría la lógica para enviar el código al correo
                Log.d("SendCode", "Correo ingresado: $email")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar código")
        }
    }
}
