package com.example.demoapp.features.pets.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.demoapp.R
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory

@Composable
fun PetListScreen(
    viewModel: PetListViewModel = hiltViewModel(),
    onNavigateToPetDetail: (String) -> Unit,
    onNavigateToCreatePet: () -> Unit = {}
) {
    val pets = viewModel.getPets()

    val customGreenDark = Color(0xFF003913)
    val customGreenLight = Color(0xFFAFD8C0)
    val customBlueLight = Color(0xFFE3F2FD)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Filtros por categoría usando chips horizontales
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Chip "Todas"
            item {
                FilterChip(
                    selected = viewModel.selectedCategory == null,
                    onClick = { viewModel.onCategorySelected(null) },
                    label = { Text("Todas") }
                )
            }
            items(PetCategory.entries) { category ->
                FilterChip(
                    selected = viewModel.selectedCategory == category,
                    onClick = { viewModel.onCategorySelected(category) },
                    label = { Text(category.label) }
                )
            }
        }

        // LISTA DE PUBLICACIONES
        if (pets.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay mascotas en esta categoría", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(pets) { pet ->
                    PetCard(
                        pet = pet,
                        onPetClick = { onNavigateToPetDetail(pet.id) },
                        onVoteClick = { viewModel.votePet(pet.id) },
                        categoryColor = customBlueLight,
                        titleColor = customGreenDark
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color,
    selectedTextColor: Color
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = selectedColor,
            selectedLabelColor = selectedTextColor,
            containerColor = Color(0xFFFFFFFF)
        ),
        border = null
    )
}

@Composable
fun PetCard(
    pet: Pet,
    onPetClick: () -> Unit,
    onVoteClick: () -> Unit,
    categoryColor: Color,
    titleColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPetClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pet.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(pet.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = titleColor)
                    Card(colors = CardDefaults.cardColors(containerColor = categoryColor)) {
                        Text(pet.category.label, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Color(0xFF1976D2))
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(pet.description, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Favorite, null, tint = Color.Red, modifier = Modifier.size(18.dp).clickable { onVoteClick() })
                        Spacer(Modifier.width(4.dp))
                        Text("${pet.votes}", style = MaterialTheme.typography.bodySmall)
                    }
                    Text("Por ${pet.ownerName}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }
        }
    }
}
