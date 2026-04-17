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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demoapp.R
import com.example.demoapp.domain.model.PetCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPetScreen(
    petId: String,
    viewModel: EditPetViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(petId) {
        viewModel.loadPet(petId)
    }

    LaunchedEffect(viewModel.updateResult) {
        viewModel.updateResult?.let { success ->
            if (success) {
                snackbarHostState.showSnackbar("✅")
                onNavigateBack()
            } else {
                viewModel.resetResult()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_pet_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
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
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.title.value,
                onValueChange = { viewModel.title.onChange(it) },
                label = { Text(stringResource(R.string.pet_form_title_publication_label)) },
                isError = viewModel.title.error != null,
                supportingText = viewModel.title.error?.let { error -> { Text(text = error) } }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.description.value,
                onValueChange = { viewModel.description.onChange(it) },
                label = { Text(stringResource(R.string.pet_form_description_detailed_label)) },
                minLines = 3,
                maxLines = 5,
                isError = viewModel.description.error != null,
                supportingText = viewModel.description.error?.let { error -> { Text(text = error) } }
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

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.animalType.value,
                onValueChange = { viewModel.animalType.onChange(it) },
                label = { Text(stringResource(R.string.pet_form_animal_type_label)) },
                isError = viewModel.animalType.error != null,
                supportingText = viewModel.animalType.error?.let { error -> { Text(text = error) } }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.breed.value,
                onValueChange = { viewModel.breed.onChange(it) },
                label = { Text(stringResource(R.string.pet_form_breed_optional_label)) }
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = viewModel.hasVaccines,
                    onCheckedChange = { viewModel.onVaccinesChanged(it) }
                )
                Text(
                    text = stringResource(R.string.pet_form_has_vaccines_label),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.photoUrl.value,
                onValueChange = { viewModel.photoUrl.onChange(it) },
                label = { Text(stringResource(R.string.pet_form_photo_url_label)) },
                isError = viewModel.photoUrl.error != null,
                supportingText = viewModel.photoUrl.error?.let { error -> { Text(text = error) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.updatePet() },
                enabled = viewModel.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(R.string.edit_pet_title))
                Text(
                    text = stringResource(R.string.pet_form_save_changes_button),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
