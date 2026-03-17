package com.example.demoapp.domain.model

/**
 * Representa una ubicación geográfica con latitud y longitud.
 * Se usa tanto para la ubicación del usuario como de las publicaciones.
 */
data class Location(
    val latitude: Double,   // Latitud de la ubicación
    val longitude: Double   // Longitud de la ubicación
)
