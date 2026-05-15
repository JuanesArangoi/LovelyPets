package com.example.demoapp.core.component

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.demoapp.R
import com.example.demoapp.domain.model.Pet
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import androidx.compose.ui.res.painterResource

/**
 * Composable reutilizable de Mapbox que muestra:
 * - Mapa centrado en Colombia (Eje Cafetero)
 * - Botón de "Mi ubicación" con manejo de permisos
 * - Marcadores de mascotas
 * - Captura de clics en el mapa
 */
@Composable
fun MapBoxComposable(
    modifier: Modifier = Modifier,
    pets: List<Pet> = emptyList(),
    showMyLocationButton: Boolean = true,
    activateClick: Boolean = false,
    onMapClickListener: (Point) -> Unit = {}
) {
    val permissionState = rememberLocationPermissionState()
    var clickedPoint by remember { mutableStateOf<Point?>(null) }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(8.0)
            center(Point.fromLngLat(-75.6491181, 4.4687891)) // Eje Cafetero, Colombia
        }
    }

    val marker = rememberIconImage(
        key = R.drawable.red_marker,
        painter = painterResource(R.drawable.red_marker)
    )

    Box(modifier = modifier) {
        MapboxMap(
            modifier = Modifier.matchParentSize(),
            mapViewportState = mapViewportState,
            onMapClickListener = { point ->
                if (activateClick) {
                    onMapClickListener(point)
                    clickedPoint = point
                }
                true
            }
        ) {
            // Marcadores de mascotas
            pets.forEach { pet ->
                if (pet.location.latitude != 0.0 && pet.location.longitude != 0.0) {
                    PointAnnotation(
                        point = Point.fromLngLat(pet.location.longitude, pet.location.latitude)
                    ) {
                        iconImage = marker
                    }
                }
            }

            // Marcador del punto clickeado
            clickedPoint?.let { point ->
                PointAnnotation(point = point) {
                    iconImage = marker
                }
            }
        }

        // Botón de mi ubicación
        if (showMyLocationButton) {
            FloatingActionButton(
                onClick = {
                    if (permissionState.hasPermission) {
                        // Seguir al usuario
                        mapViewportState.transitionToFollowPuckState()
                    } else {
                        permissionState.requestPermission()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Mi ubicación"
                )
            }
        }
    }
}

/**
 * Estado para manejar el permiso de ubicación de forma controlada.
 */
class LocationPermissionState(
    hasPermission: Boolean = false,
    val requestPermission: () -> Unit = {}
) {
    var hasPermission by mutableStateOf(hasPermission)
        internal set
    var wasJustGranted by mutableStateOf(false)
        internal set
}

@Composable
fun rememberLocationPermissionState(
    permission: String = android.Manifest.permission.ACCESS_FINE_LOCATION
): LocationPermissionState {
    val context = LocalContext.current
    val initialPermission = remember {
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
    val state = remember { LocationPermissionState(hasPermission = initialPermission) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        state.wasJustGranted = granted && !state.hasPermission
        state.hasPermission = granted
    }

    return remember(state, launcher) {
        LocationPermissionState(
            hasPermission = state.hasPermission,
            requestPermission = { launcher.launch(permission) }
        ).also { it.wasJustGranted = state.wasJustGranted }
    }
}
