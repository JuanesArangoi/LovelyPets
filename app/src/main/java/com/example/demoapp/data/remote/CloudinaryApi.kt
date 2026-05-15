package com.example.demoapp.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

/**
 * API de Cloudinary para subir imágenes con firma (signed upload).
 * Usa API key + signature generada con el API secret.
 */
interface CloudinaryApi {

    @Multipart
    @POST("v1_1/{cloudName}/image/upload")
    suspend fun uploadImage(
        @Path("cloudName") cloudName: String,
        @Part file: MultipartBody.Part,
        @Part("api_key") apiKey: RequestBody,
        @Part("timestamp") timestamp: RequestBody,
        @Part("signature") signature: RequestBody
    ): CloudinaryResponse
}

/**
 * Respuesta de la API de Cloudinary tras subir una imagen.
 */
data class CloudinaryResponse(
    val secure_url: String = "",
    val public_id: String = ""
)
