package com.example.demoapp.domain.model

/**
 * Representa un logro/insignia que el usuario puede desbloquear.
 * Los logros se activan automáticamente cuando el usuario cumple ciertas condiciones.
 */
data class Achievement(
    val id: String,              // Identificador único del logro
    val title: String,           // Nombre del logro
    val description: String,     // Descripción de cómo desbloquear el logro
    val icon: String,            // Emoji representativo del logro
    val isUnlocked: Boolean,     // Si el usuario ya desbloqueó este logro
    val progress: Int = 0,       // Progreso actual hacia el logro
    val target: Int = 1          // Meta numérica para desbloquear el logro
)
