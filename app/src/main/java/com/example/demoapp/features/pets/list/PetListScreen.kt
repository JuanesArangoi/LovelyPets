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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.demoapp.domain.model.Pet
import com.example.demoapp.domain.model.PetCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListScreen(
    viewModel: PetListViewModel = viewModel(),
    onNavigateToPetDetail: (String) -> Unit,
    onNavigateToCreatePet: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToModeratorPanel: () -> Unit,
    onLogout: () -> Unit
) {
    val pets by viewModel.pets.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    
    val customGreenDark = Color(0xFF003913)
    val customGreenLight = Color(0xFFAFD8C0)

    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Bienvenid@ a LovelyPets",
                        color = customGreenDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = customGreenLight),
                actions = {
                    IconButton(onClick = { /* Búsqueda */ }) {
                        Icon(Icons.Default.Search, "Buscar", tint = customGreenDark)
                    }
                    // Icono de NOTIFICACIONES en vez de cerrar sesión
                    IconButton(onClick = { /* Ver notificaciones */ }) {
                        Icon(Icons.Default.Notifications, "Notificaciones", tint = customGreenDark)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = customGreenLight) {
                val navItemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = customGreenDark,
                    selectedTextColor = customGreenDark,
                    indicatorColor = Color.White.copy(alpha = 0.3f),
                    unselectedIconColor = customGreenDark.copy(alpha = 0.6f),
                    unselectedTextColor = customGreenDark.copy(alpha = 0.6f)
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, "Inicio") },
                    label = { Text("Inicio") },
                    selected = selectedItem == 0,
                    onClick = { selectedItem = 0 },
                    colors = navItemColors
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LocationOn, "Mapa") },
                    label = { Text("Mapa") },
                    selected = selectedItem == 1,
                    onClick = { selectedItem = 1 },
                    colors = navItemColors
                )
                // BOTÓN DE AÑADIR EN EL CENTRO
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, "Añadir", modifier = Modifier.size(30.dp)) },
                    label = { Text("Añadir") },
                    selected = false,
                    onClick = onNavigateToCreatePet,
                    colors = navItemColors
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, "Perfil") },
                    label = { Text("Perfil") },
                    selected = selectedItem == 2,
                    onClick = { 
                        selectedItem = 2
                        onNavigateToProfile()
                    },
                    colors = navItemColors
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(PetCategory.entries) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { viewModel.filterByCategory(category) },
                        label = { Text(category.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = customGreenLight,
                            selectedLabelColor = customGreenDark
                        )
                    )
                }
            }

            if (pets.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No hay publicaciones disponibles", color = Color.Gray)
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

@Composable
fun PetCard(pet: Pet, onPetClick: () -> Unit, onVoteClick: () -> Unit) {
    val customGreenDark = Color(0xFF003913)
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onPetClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(pet.photoUrl).crossfade(true).build(),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(pet.title, fontWeight = FontWeight.Bold, color = customGreenDark)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${pet.category.label} • ${pet.animalType}", style = MaterialTheme.typography.bodySmall, color = customGreenDark.copy(alpha = 0.7f))
                    Text(if (pet.hasVaccines) "✅ Vacunado" else "❌ Sin vacunas", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Por: ${pet.ownerName}", style = MaterialTheme.typography.bodySmall)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onVoteClick() }) {
                        Icon(Icons.Default.Favorite, null, tint = Color.Red, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${pet.votes}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
