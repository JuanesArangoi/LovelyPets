package com.example.demoapp.features.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
