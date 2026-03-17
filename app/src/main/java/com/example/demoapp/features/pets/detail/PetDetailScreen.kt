package com.example.demoapp.features.pets.detail

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.demoapp.data.SessionManager
import com.example.demoapp.domain.model.Comment
import com.example.demoapp.domain.model.PetStatus

/**
 * Pantalla de detalle de una publicación de mascota.
 * Muestra toda la información, foto, comentarios y acciones disponibles.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    petId: String,                                  // ID de la mascota a mostrar
    viewModel: PetDetailViewModel = viewModel(),
    onNavigateBack: () -> Unit,                     // Volver atrás
    onNavigateToEdit: (String) -> Unit              // Navegar a editar publicación
) {
    // Cargar la mascota al entrar a la pantalla
    LaunchedEffect(petId) {
        viewModel.loadPet(petId)
    }

    val pet by viewModel.pet.collectAsState()
    val currentUser = SessionManager.currentUser

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Publicación") },
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
                },
                actions = {
                    // Botón de editar (solo visible para el dueño de la publicación)
                    if (pet != null && currentUser != null && pet!!.ownerId == currentUser.id) {
                        IconButton(onClick = { onNavigateToEdit(petId) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar publicación"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (pet == null) {
            // Mostrar mensaje si no se encontró la publicación
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No se encontró la publicación")
            }
        } else {
            val petData = pet!!

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Imagen de la mascota
                item {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(petData.photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto de ${petData.title}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                // Información principal
                item {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Título y estado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = petData.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = petData.status.label,
                                style = MaterialTheme.typography.labelMedium,
                                color = when (petData.status) {
                                    PetStatus.VERIFICADO -> MaterialTheme.colorScheme.primary
                                    PetStatus.PENDIENTE -> MaterialTheme.colorScheme.tertiary
                                    PetStatus.RECHAZADO -> MaterialTheme.colorScheme.error
                                    PetStatus.RESUELTO -> MaterialTheme.colorScheme.secondary
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Categoría y tipo
                        Text(
                            text = "${petData.category.label} • ${petData.animalType}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Raza y tamaño
                        if (petData.breed.isNotEmpty()) {
                            Text(
                                text = "Raza: ${petData.breed} • Tamaño: ${petData.size}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Vacunas
                        Text(
                            text = if (petData.hasVaccines) "✅ Vacunas al día" else "❌ Sin vacunas",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Descripción completa
                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = petData.description,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Ubicación
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Ubicación",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Lat: ${petData.location.latitude}, Lon: ${petData.location.longitude}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Publicado por
                        Text(
                            text = "Publicado por: ${petData.ownerName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botones de acción
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Botón "Me interesa adoptar" / "Me interesa"
                            Button(
                                onClick = { viewModel.votePet() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Me interesa",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Me interesa (${petData.votes})")
                            }

                            // Botón para marcar como resuelto (solo el dueño)
                            if (currentUser != null && petData.ownerId == currentUser.id
                                && petData.status != PetStatus.RESUELTO
                            ) {
                                Button(
                                    onClick = { viewModel.resolvePet() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Resuelto",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Resuelto")
                                }
                            }
                        }
                    }
                }

                // Separador antes de comentarios
                item {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }

                // Sección de comentarios
                item {
                    Text(
                        text = "Comentarios (${petData.comments.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // Lista de comentarios
                items(petData.comments) { comment ->
                    CommentItem(comment = comment)
                }

                // Campo para agregar nuevo comentario
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = viewModel.newCommentText,
                            onValueChange = { viewModel.onCommentTextChange(it) },
                            label = { Text("Escribe un comentario...") },
                            modifier = Modifier.weight(1f),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { viewModel.addComment() },
                            enabled = viewModel.newCommentText.isNotBlank()
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Enviar comentario",
                                tint = if (viewModel.newCommentText.isNotBlank())
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Espacio al final para que el contenido no quede oculto
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * Composable que muestra un comentario individual.
 */
@Composable
fun CommentItem(comment: Comment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = comment.authorName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
