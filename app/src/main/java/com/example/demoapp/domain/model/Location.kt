package com.example.demoapp.domain.model

/**
 * Representa una ubicación geográfica con latitud y longitud.
 * Valores por defecto para compatibilidad con Firestore.
 */
data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
