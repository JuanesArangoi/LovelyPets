package com.example.demoapp.domain.model

/**
 * Entidad principal del dominio: Publicación de mascota.
 * Todos los campos tienen valores por defecto para compatibilidad con Firestore.
 */
data class Pet(
    var id: String = "",                                  // Identificador único de la publicación
    val title: String = "",                               // Título de la publicación
    val description: String = "",                         // Descripción detallada
    val category: PetCategory = PetCategory.ADOPCION,     // Categoría (Adopción, Perdidos, etc.)
    val status: PetStatus = PetStatus.PENDIENTE,          // Estado de verificación
    val animalType: String = "",                          // Tipo de animal (perro, gato, etc.)
    val breed: String = "",                               // Raza aproximada
    val size: String = "",                                // Tamaño (pequeño, mediano, grande)
    val hasVaccines: Boolean = false,                     // Si tiene vacunas al día
    val photoUrl: String = "",                            // URL de la foto de la mascota
    val location: Location = Location(),                  // Ubicación geográfica
    val ownerId: String = "",                             // ID del usuario que creó la publicación
    val ownerName: String = "",                           // Nombre del dueño de la publicación
    val votes: Int = 0,                                   // Cantidad de votos "Me interesa"
    val viewCount: Int = 0,                               // Contador de visualizaciones
    val rejectionReason: String = "",                     // Motivo de rechazo (si aplica)
    val comments: List<Comment> = emptyList(),            // Lista de comentarios
    val createdAt: Long = System.currentTimeMillis()      // Fecha de creación en milisegundos
)
