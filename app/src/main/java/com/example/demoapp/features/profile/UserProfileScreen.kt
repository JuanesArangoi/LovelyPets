package com.example.demoapp.features.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    onAccountDeleted: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToEdit: (String) -> Unit = {},
    onNavigateToPetDetail: (String) -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDeletePetDialog by remember { mutableStateOf<String?>(null) }

    val currentUser = viewModel.getCurrentUser()

    // Colores personalizados
    val customGreenDark = Color(0xFF003913)
    val customGreenLight = Color(0xFFAFD8C0)
    val customBlueLight = Color(0xFFE3F2FD)

    LaunchedEffect(Unit) { viewModel.loadUserData() }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.profile_delete_confirm_title), fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(R.string.profile_delete_confirm_message)) },
            confirmButton = {
                TextButton(onClick = { viewModel.deleteAccount(); showDeleteDialog = false }) {
                    Text(stringResource(R.string.profile_confirm_button), color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.profile_cancel_button), color = customGreenDark)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile_title), color = customGreenDark, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = customGreenLight),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = customGreenDark)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Cerrar sesión", tint = customGreenDark)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
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
                        Text(stringResource(R.string.profile_email_label), color = Color.Gray)
                        Text(currentUser.email, fontWeight = FontWeight.Bold, color = customGreenDark)
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
                        Text(stringResource(R.string.profile_level_label, currentUser.level.label), fontWeight = FontWeight.Bold, color = customGreenDark)
                        Text(stringResource(R.string.profile_points_label, currentUser.points), fontWeight = FontWeight.Bold, color = customGreenDark)
                    }
                }
            }

            Text("📊 Resumen", fontWeight = FontWeight.Bold, color = customGreenDark)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCardItem(Modifier.weight(1f), stringResource(R.string.pet_card_status_label), viewModel.getActivePetsCount().toString(), customBlueLight, Color(0xFF1976D2))
                StatCardItem(Modifier.weight(1f), stringResource(R.string.pet_detail_resolved), viewModel.getResolvedPetsCount().toString(), customGreenLight, customGreenDark)
            }

            // ===== MIS PUBLICACIONES =====
            Text("📋 Mis publicaciones", fontWeight = FontWeight.Bold, color = customGreenDark)

            val userPets = viewModel.getUserPets()
            if (userPets.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = customBlueLight)
                ) {
                    Text(
                        "No tienes publicaciones aún",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                }
            } else {
                userPets.forEach { pet ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToPetDetail(pet.id) },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(pet.title, fontWeight = FontWeight.Bold, color = customGreenDark,
                                    maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                Text("${pet.category.label} • ${pet.status.label}",
                                    style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            Row {
                                IconButton(onClick = { onNavigateToEdit(pet.id) }) {
                                    Icon(Icons.Default.Edit, "Editar", tint = customGreenDark, modifier = Modifier.size(20.dp))
                                }
                                IconButton(onClick = { showDeletePetDialog = pet.id }) {
                                    Icon(Icons.Default.Delete, "Eliminar", tint = Color.Red, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Diálogo de confirmación de eliminar publicación
            showDeletePetDialog?.let { petId ->
                AlertDialog(
                    onDismissRequest = { showDeletePetDialog = null },
                    title = { Text(stringResource(R.string.pet_detail_delete_confirm_title), fontWeight = FontWeight.Bold) },
                    text = { Text(stringResource(R.string.pet_detail_delete_confirm_message)) },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deletePet(petId)
                            showDeletePetDialog = null
                        }) {
                            Text(stringResource(R.string.pet_detail_delete_button), color = Color.Red, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeletePetDialog = null }) {
                            Text(stringResource(R.string.profile_cancel_button), color = customGreenDark)
                        }
                    }
                )
            }

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
                label = { Text(stringResource(R.string.profile_name_label)) },
                colors = fieldColors
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.phone.value,
                onValueChange = { viewModel.phone.onChange(it) },
                label = { Text(stringResource(R.string.profile_phone_label)) },
                colors = fieldColors
            )

            Button(
                onClick = { viewModel.updateProfile() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = customGreenLight, contentColor = customGreenDark)
            ) {
                Text(stringResource(R.string.profile_save_button), fontWeight = FontWeight.Bold)
            }

            // Botón Cerrar sesión en el cuerpo de la pantalla
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = customGreenDark),
                border = BorderStroke(1.dp, customGreenLight)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión", fontWeight = FontWeight.Bold)
            }

            // Botón Eliminar cuenta con fondo blanco
            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Red),
                border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.3f))
            ) {
                Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.profile_delete_account_button))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatCardItem(modifier: Modifier, label: String, value: String, bgColor: Color, txtColor: Color) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = bgColor)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = txtColor)
            Text(label, style = MaterialTheme.typography.bodySmall, color = txtColor.copy(alpha = 0.8f))
        }
    }
}
