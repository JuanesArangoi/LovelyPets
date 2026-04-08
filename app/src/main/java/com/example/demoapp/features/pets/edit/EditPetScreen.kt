package com.example.demoapp.features.pets.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demoapp.domain.model.PetCategory

/**
 * Pantalla para editar una publicación de mascota existente.
 * Pre-carga los datos actuales de la publicación en el formulario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPetScreen(
    petId: String,
    viewModel: EditPetViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Cargar datos de la mascota al entrar
    LaunchedEffect(petId) {
        viewModel.loadPet(petId)
    }

    // Efecto para mostrar resultado de la edición
    LaunchedEffect(viewModel.updateResult) {
        viewModel.updateResult?.let { success ->
            if (success) {
                snackbarHostState.showSnackbar("¡Publicación actualizada exitosamente!")
                onNavigateBack()
            } else {
                snackbarHostState.showSnackbar("Error al actualizar la publicación")
                viewModel.resetResult()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Editar Publicación") },
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
            // Campo: Título
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.title.value,
                onValueChange = { viewModel.title.onChange(it) },
                label = { Text("Título de la publicación") },
                isError = viewModel.title.error != null,
                supportingText = viewModel.title.error?.let { error ->
                    { Text(text = error) }
                }
            )

            // Campo: Descripción
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.description.value,
                onValueChange = { viewModel.description.onChange(it) },
                label = { Text("Descripción detallada") },
                minLines = 3,
                maxLines = 5,
                isError = viewModel.description.error != null,
                supportingText = viewModel.description.error?.let { error ->
                    { Text(text = error) }
                }
            )

            // Dropdown: Categoría
            var expandedCategory by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = it }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = viewModel.selectedCategory.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) }
                )
                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }
                ) {
                    PetCategory.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.label) },
                            onClick = {
                                viewModel.onCategorySelected(category)
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            // Campo: Tipo de animal
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.animalType.value,
                onValueChange = { viewModel.animalType.onChange(it) },
                label = { Text("Tipo de animal") },
                isError = viewModel.animalType.error != null,
                supportingText = viewModel.animalType.error?.let { error ->
                    { Text(text = error) }
                }
            )

            // Campo: Raza
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.breed.value,
                onValueChange = { viewModel.breed.onChange(it) },
                label = { Text("Raza aproximada (opcional)") }
            )

            // Dropdown: Tamaño
            var expandedSize by remember { mutableStateOf(false) }
            val sizes = listOf("Pequeño", "Mediano", "Grande")
            ExposedDropdownMenuBox(
                expanded = expandedSize,
                onExpandedChange = { expandedSize = it }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = viewModel.size.value,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tamaño") },
                    isError = viewModel.size.error != null,
                    supportingText = viewModel.size.error?.let { error ->
                        { Text(text = error) }
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSize) }
                )
                ExposedDropdownMenu(
                    expanded = expandedSize,
                    onDismissRequest = { expandedSize = false }
                ) {
                    sizes.forEach { sizeOption ->
                        DropdownMenuItem(
                            text = { Text(sizeOption) },
                            onClick = {
                                viewModel.size.onChange(sizeOption)
                                expandedSize = false
                            }
                        )
                    }
                }
            }

            // Checkbox: Vacunas
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = viewModel.hasVaccines,
                    onCheckedChange = { viewModel.onVaccinesChanged(it) }
                )
                Text(
                    text = "¿Tiene vacunas al día?",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Campo: URL de la foto
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.photoUrl.value,
                onValueChange = { viewModel.photoUrl.onChange(it) },
                label = { Text("URL de la foto") },
                isError = viewModel.photoUrl.error != null,
                supportingText = viewModel.photoUrl.error?.let { error ->
                    { Text(text = error) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de guardar cambios
            Button(
                onClick = { viewModel.updatePet() },
                enabled = viewModel.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Guardar"
                )
                Text(
                    text = "  Guardar cambios",
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
