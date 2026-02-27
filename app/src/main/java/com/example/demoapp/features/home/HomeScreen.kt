package com.example.demoapp.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demoapp.R

@Composable
fun HomeScreen() {

//    Button(
//        modifier = Modifier
//            .size(width = 200.dp, height = 60.dp),
//        onClick = {}
//
//    ){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically), // Espacio entre elementos y centrado vertical
    ) {

        Image(
            painter = painterResource(R.drawable.cait),
            contentDescription = "logo"

        )
        Text(
            modifier = Modifier.width(200.dp),
            text = "Hola esta aplicacion es de animales, diseñada para :)",
                    fontSize = 25.sp

        )

        Button(
            modifier = Modifier
                .size(width = 200.dp, height = 60.dp),
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Icono de persona"
            )

            Text(
                text = "Iniciar Sesión"
            )
        }
        Button(
            modifier = Modifier
                .size(width = 200.dp, height = 60.dp),
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Icono de favorito"
            )

            Text(
                text = "Crear Cuenta"
            )
        }
    }
}
