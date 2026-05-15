package com.example.demoapp.domain.repository

import android.net.Uri

/**
 * Repositorio para la subida de imágenes a Cloudinary.
 */
interface CloudinaryRepository {
    suspend fun uploadImage(imageUri: Uri): String
}
