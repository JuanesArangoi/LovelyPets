package com.example.demoapp.features.login

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import com.example.demoapp.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun Loginmejorado() {

    Log.e("LoginMejorado", "Recomposición")

    val localContext = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorEmail by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically), // Espacio entre elementos y centrado vertical
        ) {

        Image(
            painter = painterResource(R.mipmap.mascota),
            contentDescription = "Welcome Image"
        )

        Text(
            text = "Inicio de Sesión",
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

        OutlinedTextField(
            label = {
                Text(
                    text = "password"
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Icono de password"
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            value = password,
            onValueChange = {
                password = it
            },
        )

        Button(
            onClick = {
                if (email == "william@gmail.com" &&
                    password == "123") {
                    Toast.makeText(
                        localContext,
                        "Login Correcto",
                        Toast.LENGTH_LONG
                    ).show()
                }else{
                    Toast.makeText(
                        localContext,
                        "Datos No Válidos",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
        ) {
            Text("Iniciar sesión")
        }
    }
}
