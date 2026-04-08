package com.example.demoapp.features.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demoapp.data.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onAccountDeleted: () -> Unit,
    onLogout: () -> Unit // Nueva callback para logout
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val currentUser = SessionManager.currentUser

    val customGreenDark = Color(0xFF003913)
    val customGreenLight = Color(0xFFAFD8C0)
    val customBlueLight = Color(0xFFE3F2FD)

    LaunchedEffect(Unit) { viewModel.loadProfile() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold, color = customGreenDark) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = customGreenLight),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = customGreenDark)
                    }
                },
                actions = {
                    // BOTÓN DE CERRAR SESIÓN AQUÍ
                    IconButton(onClick = {
                        SessionManager.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.ExitToApp, "Cerrar sesión", tint = customGreenDark)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (currentUser != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = customBlueLight)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Correo electrónico", color = Color.Gray)
                        Text(currentUser.email, fontWeight = FontWeight.Bold)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = customGreenLight)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Nivel", color = customGreenDark)
                            Text(currentUser.level.label, fontWeight = FontWeight.ExtraBold, color = customGreenDark)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Puntos", color = customGreenDark)
                            Text("${currentUser.points} pts", fontWeight = FontWeight.ExtraBold, color = customGreenDark)
                        }
                    }
                }
            }

            Text("📊 Resumen", fontWeight = FontWeight.Bold, color = customGreenDark)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard(label = "Activas", value = viewModel.activePets.toString(), bgColor = customBlueLight, txtColor = Color(0xFF1976D2))
                StatCard(label = "Listas", value = viewModel.resolvedPets.toString(), bgColor = customGreenLight, txtColor = customGreenDark)
            }

            Text("✏️ Editar datos", fontWeight = FontWeight.Bold, color = customGreenDark)
            
            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = customGreenDark,
                focusedLabelColor = customGreenDark,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.name.value,
                onValueChange = { viewModel.name.onChange(it) },
                label = { Text("Nombre completo") },
                colors = fieldColors
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.phone.value,
                onValueChange = { viewModel.phone.onChange(it) },
                label = { Text("Teléfono") },
                colors = fieldColors
            )

            Button(
                onClick = { viewModel.saveProfile() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = customGreenLight, contentColor = customGreenDark)
            ) {
                Text("Guardar cambios", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE), contentColor = Color.Red)
            ) {
                Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Eliminar mi cuenta")
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, bgColor: Color, txtColor: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = txtColor)
            Text(label, style = MaterialTheme.typography.bodySmall, color = txtColor.copy(alpha = 0.8f))
        }
    }
}
