package com.example.demoapp.features.pets.create

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.demoapp.R
import com.example.demoapp.core.component.MapBoxComposable
import com.example.demoapp.domain.model.PetCategory
import com.mapbox.geojson.Point

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePetScreen(
    viewModel: CreatePetViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoading by viewModel.isLoading.collectAsState()
    val isUploadingImage by viewModel.isUploadingImage.collectAsState()

    val customGreenDark = Color(0xFF003913)
    val customGreenLight = Color(0xFFAFD8C0)

    // Launcher para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadImage(it) }
    }

    LaunchedEffect(viewModel.createResult) {
        viewModel.createResult?.let { success ->
            if (success) {
                snackbarHostState.showSnackbar("✅ Publicación creada exitosamente")
                onNavigateBack()
            } else {
                snackbarHostState.showSnackbar("❌ Error al crear la publicación")
                viewModel.resetResult()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.create_pet_title), color = customGreenDark, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = customGreenLight
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description),
                            tint = customGreenDark
                        )
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = customGreenDark,
                focusedLabelColor = customGreenDark,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.title.value,
                onValueChange = { viewModel.title.onChange(it) },
                label = { Text(stringResource(R.string.pet_form_title_publication_label)) },
                isError = viewModel.title.error != null,
                supportingText = viewModel.title.error?.let { error -> { Text(text = error) } },
                colors = textFieldColors
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.description.value,
                onValueChange = { viewModel.description.onChange(it) },
                label = { Text(stringResource(R.string.pet_form_description_detailed_label)) },
                minLines = 3,
                maxLines = 5,
                isError = viewModel.description.error != null,
                supportingText = viewModel.description.error?.let { error -> { Text(text = error) } },
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
                    label = { Text(stringResource(R.string.pet_form_category_label)) },
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
                            onClick = {
                                viewModel.onCategorySelected(category)
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.animalType.value,
                onValueChange = { viewModel.animalType.onChange(it) },
                label = { Text(stringResource(R.string.pet_form_animal_type_label)) },
                isError = viewModel.animalType.error != null,
                supportingText = viewModel.animalType.error?.let { error -> { Text(text = error) } },
                colors = textFieldColors
            )

            var expandedSize by remember { mutableStateOf(false) }
            val sizes = listOf(
                stringResource(R.string.pet_form_size_small),
                stringResource(R.string.pet_form_size_medium),
                stringResource(R.string.pet_form_size_large)
            )
            ExposedDropdownMenuBox(
                expanded = expandedSize,
                onExpandedChange = { expandedSize = it }
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    value = viewModel.size.value,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.pet_form_size_label)) },
                    isError = viewModel.size.error != null,
                    supportingText = viewModel.size.error?.let { error -> { Text(text = error) } },
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
                            onClick = {
                                viewModel.size.onChange(sizeOption)
                                expandedSize = false
                            }
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = viewModel.hasVaccines,
                    onCheckedChange = { viewModel.onVaccinesChanged(it) },
                    colors = CheckboxDefaults.colors(checkedColor = customGreenDark)
                )
                Text(stringResource(R.string.pet_form_has_vaccines_label), style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }

            // --- Sección de imagen con Cloudinary ---
            Text(
                text = "Foto de la mascota",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = customGreenDark
            )

            if (viewModel.photoUrl.value.isNotEmpty()) {
                AsyncImage(
                    model = viewModel.photoUrl.value,
                    contentDescription = "Foto de la mascota",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, customGreenLight, RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            OutlinedButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isUploadingImage
            ) {
                if (isUploadingImage) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = customGreenDark
                    )
                    Text("  Subiendo imagen...", color = customGreenDark)
                } else {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = null,
                        tint = customGreenDark
                    )
                    Text(
                        if (viewModel.photoUrl.value.isEmpty()) "  Seleccionar imagen de galería"
                        else "  Cambiar imagen",
                        color = customGreenDark
                    )
                }
            }

            // --- Mini mapa para ubicación ---
            Text(
                text = "Ubicación (toca el mapa para seleccionar)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = customGreenDark
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, customGreenLight, RoundedCornerShape(12.dp))
            ) {
                MapBoxComposable(
                    modifier = Modifier.fillMaxSize(),
                    activateClick = true,
                    showMyLocationButton = true,
                    onMapClickListener = { point: Point ->
                        viewModel.onLocationSelected(point.latitude(), point.longitude())
                    }
                )
            }

            Text(
                text = "📍 Lat: ${"%.4f".format(viewModel.selectedLatitude)}, Lng: ${"%.4f".format(viewModel.selectedLongitude)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.createPet() },
                enabled = viewModel.isFormValid && !isLoading && !isUploadingImage,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = customGreenLight, contentColor = customGreenDark),
                shape = MaterialTheme.shapes.medium
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = customGreenDark
                    )
                } else {
                    Icon(imageVector = Icons.Default.Create, contentDescription = null)
                    Text(stringResource(R.string.pet_form_publish_button), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
