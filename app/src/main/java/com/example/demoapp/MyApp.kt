package com.example.demoapp

import android.app.Application
import com.example.demoapp.data.repository.SeedDataProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mapbox.common.MapboxOptions
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Clase principal de la aplicación, anotada con @HiltAndroidApp
 * para inicializar Hilt y habilitar la inyección de dependencias.
 * Ejecuta la siembra de datos de ejemplo en Firestore si es necesario.
 * Inicializa el token de Mapbox para el mapa.
 */
@HiltAndroidApp
class MyApp : Application() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    override fun onCreate() {
        super.onCreate()

        // Inicializar Mapbox con el access token de recursos
        try {
            val mapboxToken = getString(R.string.mapbox_access_token)
            if (mapboxToken.isNotBlank()) {
                MapboxOptions.accessToken = mapboxToken
            }
        } catch (_: Exception) {
            // Token no configurado, el mapa mostrará un fallback
        }

        // Sembrar datos de ejemplo si Firestore está vacío
        CoroutineScope(Dispatchers.IO).launch {
            try {
                SeedDataProvider.seedIfEmpty(firestore)
            } catch (_: Exception) {
                // Silenciar errores de seed (primera ejecución sin conexión, etc.)
            }
        }
    }
}
