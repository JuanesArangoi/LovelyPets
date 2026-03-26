package com.example.demoapp.features.pets.detail

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.demoapp.domain.model.Comment
import com.example.demoapp.domain.model.PetStatus

/**
 * Pantalla de detalle de una publicación de mascota.
 * Usa hiltViewModel() para inyectar PetDetailViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    petId: String,
    viewModel: PetDetailViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit
) {
    // Cargar la mascota al entrar
    LaunchedEffect(petId) {
        viewModel.loadPet(petId)
    }

    val pet = viewModel.pet
    val comments = viewModel.getComments()

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
                    if (pet != null) {
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Imagen
                item {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pet.photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto de ${pet.title}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                // Información principal
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pet.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = pet.status.label,
                                style = MaterialTheme.typography.labelMedium,
                                color = when (pet.status) {
                                    PetStatus.VERIFICADO -> MaterialTheme.colorScheme.primary
                                    PetStatus.PENDIENTE -> MaterialTheme.colorScheme.tertiary
                                    PetStatus.RECHAZADO -> MaterialTheme.colorScheme.error
                                    PetStatus.RESUELTO -> MaterialTheme.colorScheme.secondary
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${pet.category.label} • ${pet.animalType}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        if (pet.breed.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Raza: ${pet.breed} • Tamaño: ${pet.size}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Text(
                            text = if (pet.hasVaccines) "✅ Vacunas al día" else "❌ Sin vacunas",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = pet.description, style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Ubicación",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Lat: ${pet.location.latitude}, Lon: ${pet.location.longitude}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Publicado por: ${pet.ownerName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botones de acción
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
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
                                Text("Me interesa (${pet.votes})")
                            }

                            if (pet.status != PetStatus.RESUELTO) {
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

                // Separador
                item {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }

                // Sección de comentarios
                item {
                    Text(
                        text = "Comentarios (${comments.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(comments) { comment ->
                    CommentItem(comment = comment)
                }

                // Campo para nuevo comentario
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = viewModel.commentText,
                            onValueChange = { viewModel.onCommentTextChange(it) },
                            label = { Text("Escribe un comentario...") },
                            modifier = Modifier.weight(1f),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { viewModel.addComment() },
                            enabled = viewModel.commentText.isNotBlank()
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Enviar comentario",
                                tint = if (viewModel.commentText.isNotBlank())
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
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
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = comment.authorName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = comment.text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
