package com.example.demoapp.domain.model

/**
 * Estados posibles de una publicación de mascota.
 * Controla el ciclo de vida de cada publicación en la plataforma.
 */
enum class PetStatus(val label: String) {
    PENDIENTE("Pendiente"),       // Publicación creada, aún no verificada por un moderador
    VERIFICADO("Verificado"),     // Publicación verificada por un moderador
    RECHAZADO("Rechazado"),       // Publicación rechazada por un moderador
    RESUELTO("Resuelto")          // Publicación marcada como resuelta/finalizada
}
