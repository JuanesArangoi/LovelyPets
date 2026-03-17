package com.example.demoapp.domain.model

/**
 * Niveles de usuario según puntos acumulados.
 * Los niveles se asignan automáticamente según la participación del usuario.
 */
enum class UserLevel(val label: String, val minPoints: Int) {
    AMIGO_ANIMAL("Amigo Animal", 0),        // Nivel inicial
    PROTECTOR("Protector", 50),              // 50+ puntos
    GUARDIAN("Guardián", 150),               // 150+ puntos
    HEROE_MASCOTAS("Héroe de las Mascotas", 300) // 300+ puntos
}
