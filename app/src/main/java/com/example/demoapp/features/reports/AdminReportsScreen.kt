package com.example.demoapp.features.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.PlaylistAddCheck
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.demoapp.R
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Pantalla de reportes del administrador.
 * Muestra una cuadrícula con estadísticas y un listado de publicaciones recientes.
 */
@Composable
fun AdminReportsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: AdminReportsViewModel = hiltViewModel()
) {
    val resolved = viewModel.getResolvedCount()
    val unresolved = viewModel.getUnresolvedCount()
    val active = viewModel.getActiveCount()
    val total = viewModel.getTotalCount()
    val pending = viewModel.getPendingCount()
    val rejected = viewModel.getRejectedCount()
    val recentPets = viewModel.getRecentPets()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Título ──
        item {
            Text(
                text = stringResource(R.string.reports_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // ── Cuadrícula de estadísticas (2 columnas) ──
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.reports_active),
                    count = active,
                    icon = Icons.Outlined.Pets,
                    backgroundColor = Color(0xFF1B5E20),
                    iconTint = Color(0xFF81C784)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.reports_resolved),
                    count = resolved,
                    icon = Icons.Default.CheckCircle,
                    backgroundColor = Color(0xFF0D47A1),
                    iconTint = Color(0xFF64B5F6)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.reports_unresolved),
                    count = unresolved,
                    icon = Icons.Default.Warning,
                    backgroundColor = Color(0xFFE65100),
                    iconTint = Color(0xFFFFB74D)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.reports_total),
                    count = total,
                    icon = Icons.Outlined.PlaylistAddCheck,
                    backgroundColor = Color(0xFF4A148C),
                    iconTint = Color(0xFFCE93D8)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.reports_pending),
                    count = pending,
                    icon = Icons.Default.Warning,
                    backgroundColor = Color(0xFFF57F17),
                    iconTint = Color(0xFFFFF176)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.reports_rejected),
                    count = rejected,
                    icon = Icons.Default.Warning,
                    backgroundColor = Color(0xFFB71C1C),
                    iconTint = Color(0xFFEF9A9A)
                )
            }
        }

        // ── Sección de publicaciones recientes ──
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.reports_recent_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (recentPets.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.reports_no_recent),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
        } else {
            items(recentPets) { pet ->
                RecentPetItem(pet = pet)
            }
        }

        // Espacio extra al final para que el bottom bar no tape
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

// ──────────────────────────────────────────────
// Componentes internos
// ──────────────────────────────────────────────

/**
 * Tarjeta de estadística con icono, número y etiqueta.
 */
@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    count: Int,
    icon: ImageVector,
    backgroundColor: Color,
    iconTint: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$count",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Elemento de la lista de publicaciones recientes.
 */
@Composable
private fun RecentPetItem(pet: Pet) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("es", "CO"))
    val dateString = dateFormat.format(Date(pet.createdAt))

    val statusColor = when (pet.status) {
        PetStatus.VERIFICADO -> Color(0xFF2E7D32)
        PetStatus.PENDIENTE -> Color(0xFFF9A825)
        PetStatus.RECHAZADO -> Color(0xFFC62828)
        PetStatus.RESUELTO -> Color(0xFF1565C0)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Miniatura
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pet.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = pet.title,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Información principal
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pet.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${pet.category.label} • ${pet.ownerName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Chip de estado
            Box(
                modifier = Modifier
                    .background(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = pet.status.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
