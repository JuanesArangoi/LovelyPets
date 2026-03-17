package com.example.demoapp.domain.model

/**
 * Categorías de publicaciones de mascotas.
 * Cada publicación debe pertenecer a una categoría según su propósito.
 */
enum class PetCategory(val label: String) {
    ADOPCION("Adopción"),         // Mascotas disponibles para adoptar
    PERDIDOS("Perdidos"),         // Mascotas que se extraviaron
    ENCONTRADOS("Encontrados"),   // Mascotas encontradas en la calle
    TEMPORAL("Hogar temporal"),   // Hogares de paso temporales
    VETERINARIA("Veterinaria")   // Jornadas de vacunación, esterilización gratuita
}
