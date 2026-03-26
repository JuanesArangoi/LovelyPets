package com.example.demoapp.features.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Pantalla de perfil del usuario.
 * Permite editar datos personales, ver estadísticas y eliminar la cuenta.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    onAccountDeleted: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val currentUser = viewModel.getCurrentUser()

    // Cargar perfil al entrar
    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    // Manejar resultado de edición
    LaunchedEffect(viewModel.updateResult) {
        viewModel.updateResult?.let { success ->
            if (success) {
                snackbarHostState.showSnackbar("¡Perfil actualizado exitosamente!")
                viewModel.resetUpdateResult()
            }
        }
    }

    // Manejar resultado de eliminación
    LaunchedEffect(viewModel.deleteResult) {
        viewModel.deleteResult?.let { success ->
            if (success) {
                onAccountDeleted()
            }
            viewModel.resetDeleteResult()
        }
    }

    // Diálogo de confirmación para eliminar cuenta
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar cuenta") },
            text = { Text("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAccount()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Información del usuario
            if (currentUser != null) {
                // Email (no editable)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Correo electrónico",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = currentUser.email,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Nivel y puntos
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Nivel",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = currentUser.level.label,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Puntos",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = "${currentUser.points}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Rol del usuario
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Rol: ${currentUser.role.label}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Estadísticas
            Text(
                text = "📊 Mis estadísticas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard(label = "Activas", value = viewModel.getActivePetsCount().toString())
                StatCard(label = "Resueltas", value = viewModel.getResolvedPetsCount().toString())
                StatCard(label = "Pendientes", value = viewModel.getPendingPetsCount().toString())
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Campos editables
            Text(
                text = "✏️ Editar datos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.name.value,
                onValueChange = { viewModel.name.onChange(it) },
                label = { Text("Nombre completo") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Nombre")
                },
                isError = viewModel.name.error != null,
                supportingText = viewModel.name.error?.let { error ->
                    { Text(text = error) }
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.phone.value,
                onValueChange = { viewModel.phone.onChange(it) },
                label = { Text("Teléfono") },
                isError = viewModel.phone.error != null,
                supportingText = viewModel.phone.error?.let { error ->
                    { Text(text = error) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.city.value,
                onValueChange = { viewModel.city.onChange(it) },
                label = { Text("Ciudad") },
                isError = viewModel.city.error != null,
                supportingText = viewModel.city.error?.let { error ->
                    { Text(text = error) }
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.address.value,
                onValueChange = { viewModel.address.onChange(it) },
                label = { Text("Dirección (opcional)") }
            )

            // Botón guardar cambios
            Button(
                onClick = { viewModel.updateProfile() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón eliminar cuenta
            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar cuenta"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Eliminar mi cuenta")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Tarjeta de estadística individual.
 */
@Composable
fun StatCard(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
