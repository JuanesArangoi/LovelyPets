package com.example.demoapp.features.map

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demoapp.core.component.MapBoxComposable

@Composable
fun MapScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: MapViewModel = hiltViewModel()
) {
    val verifiedPets by viewModel.verifiedPets.collectAsState()

    MapBoxComposable(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        pets = verifiedPets,
        showMyLocationButton = true
    )
}
