package com.example.demoapp.features.pets.list

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.demoapp.data.SessionManager
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory
import com.example.demoapp.domain.model.UserRole

/**
 * Pantalla principal del feed de publicaciones de mascotas.
 * Muestra una lista con filtros por categoría y acceso a crear publicaciones.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListScreen(
    viewModel: PetListViewModel = viewModel(),
    onNavigateToPetDetail: (String) -> Unit,   // Navegar al detalle de una mascota
    onNavigateToCreatePet: () -> Unit,          // Navegar a crear publicación
    onNavigateToProfile: () -> Unit,            // Navegar al perfil del usuario
    onNavigateToModeratorPanel: () -> Unit,     // Navegar al panel del moderador
    onLogout: () -> Unit                        // Cerrar sesión
) {
    // Obtener datos del ViewModel
    val pets by viewModel.pets.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val currentUser = SessionManager.currentUser

    Scaffold(
        // Barra superior con título y acciones
        topBar = {
            TopAppBar(
                title = { Text("🐾 LovelyPets") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    // Botón de perfil
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil"
                        )
                    }
                    // Botón de panel de moderador (solo visible para moderadores)
                    if (currentUser?.role == UserRole.MODERADOR) {
                        IconButton(onClick = onNavigateToModeratorPanel) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Panel de moderador"
                            )
                        }
                    }
                    // Botón de cerrar sesión
                    IconButton(onClick = {
                        SessionManager.logout()
                        onLogout()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        },
        // Botón flotante para crear nueva publicación
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreatePet,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear publicación"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filtros por categoría usando chips horizontales
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(PetCategory.entries) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { viewModel.filterByCategory(category) },
                        label = { Text(category.label) }
                    )
                }
            }

            // Lista de publicaciones de mascotas
            if (pets.isEmpty()) {
                // Mensaje cuando no hay publicaciones
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No hay publicaciones disponibles",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pets) { pet ->
                        PetCard(
                            pet = pet,
                            onPetClick = { onNavigateToPetDetail(pet.id) },
                            onVoteClick = { viewModel.votePet(pet.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tarjeta que muestra la información resumida de una publicación de mascota.
 */
@Composable
fun PetCard(
    pet: Pet,
    onPetClick: () -> Unit,     // Acción al hacer clic en la tarjeta
    onVoteClick: () -> Unit     // Acción al votar "Me interesa"
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPetClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Imagen de la mascota
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pet.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto de ${pet.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )

            // Información de la publicación
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Título
                Text(
                    text = pet.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Categoría y tipo de animal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${pet.category.label} • ${pet.animalType}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (pet.hasVaccines) "✅ Vacunado" else "❌ Sin vacunas",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Descripción
                Text(
                    text = pet.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Fila inferior: publicado por + votos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Por: ${pet.ownerName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Botón de "Me interesa" con contador de votos
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onVoteClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Me interesa",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${pet.votes}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
