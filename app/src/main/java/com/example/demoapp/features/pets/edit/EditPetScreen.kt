package com.example.demoapp.features.pets.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demoapp.domain.model.PetCategory

/**
 * Pantalla para editar una publicación de mascota existente con estilo visual actualizado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPetScreen(
    petId: String,
    viewModel: EditPetViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Colores personalizados
    val customGreenDark = Color(0xFF003913)
    val customGreenLight = Color(0xFFAFD8C0)

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = customGreenDark,
        focusedLabelColor = customGreenDark,
        focusedLeadingIconColor = customGreenDark,
        cursorColor = customGreenDark,
        unfocusedContainerColor = Color.White,
        focusedContainerColor = Color.White
    )

    LaunchedEffect(petId) { viewModel.loadPet(petId) }

    LaunchedEffect(viewModel.editResult) {
        viewModel.editResult?.let { success ->
            if (success) {
                snackbarHostState.showSnackbar("¡Publicación actualizada!")
                onNavigateBack()
            } else {
                snackbarHostState.showSnackbar("Error al actualizar")
                viewModel.resetResult()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Editar Publicación", fontWeight = FontWeight.Bold, color = customGreenDark) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = customGreenLight),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = customGreenDark)
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
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.title.value,
                onValueChange = { viewModel.title.onChange(it) },
                label = { Text("Título de la publicación") },
                colors = textFieldColors
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.description.value,
                onValueChange = { viewModel.description.onChange(it) },
                label = { Text("Descripción detallada") },
                minLines = 3,
                colors = textFieldColors
            )

            var expandedCategory by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = it }
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    value = viewModel.selectedCategory.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    colors = textFieldColors
                )
                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }
                ) {
                    PetCategory.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.label) },
                            onClick = { viewModel.onCategoryChange(category); expandedCategory = false }
                        )
                    }
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.animalType.value,
                onValueChange = { viewModel.animalType.onChange(it) },
                label = { Text("Tipo de animal") },
                colors = textFieldColors
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.breed.value,
                onValueChange = { viewModel.breed.onChange(it) },
                label = { Text("Raza aproximada (opcional)") },
                colors = textFieldColors
            )

            var expandedSize by remember { mutableStateOf(false) }
            val sizes = listOf("Pequeño", "Mediano", "Grande")
            ExposedDropdownMenuBox(
                expanded = expandedSize,
                onExpandedChange = { expandedSize = it }
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    value = viewModel.size.value,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tamaño") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSize) },
                    colors = textFieldColors
                )
                ExposedDropdownMenu(
                    expanded = expandedSize,
                    onDismissRequest = { expandedSize = false }
                ) {
                    sizes.forEach { sizeOption ->
                        DropdownMenuItem(
                            text = { Text(sizeOption) },
                            onClick = { viewModel.size.onChange(sizeOption); expandedSize = false }
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = viewModel.hasVaccines,
                    onCheckedChange = { viewModel.onVaccinesChange(it) },
                    colors = CheckboxDefaults.colors(checkedColor = customGreenDark)
                )
                Text("¿Tiene vacunas al día?", color = customGreenDark)
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.photoUrl.value,
                onValueChange = { viewModel.photoUrl.onChange(it) },
                label = { Text("URL de la foto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                colors = textFieldColors
            )

            Button(
                onClick = { viewModel.savePet() },
                enabled = viewModel.isFormValid,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = customGreenLight, contentColor = customGreenDark)
            ) {
                Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                Text("  Guardar cambios", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
