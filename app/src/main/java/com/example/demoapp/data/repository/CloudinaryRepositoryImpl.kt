package com.example.demoapp.data.repository

import android.content.Context
import android.net.Uri
import com.example.demoapp.BuildConfig
import com.example.demoapp.data.remote.CloudinaryApi
import com.example.demoapp.domain.repository.CloudinaryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación de CloudinaryRepository.
 * Sube imágenes a Cloudinary usando signed upload con API key y secret.
 */
@Singleton
class CloudinaryRepositoryImpl @Inject constructor(
    private val cloudinaryApi: CloudinaryApi,
    @ApplicationContext private val context: Context
) : CloudinaryRepository {

    companion object {
        private val CLOUD_NAME = BuildConfig.CLOUDINARY_CLOUD_NAME
        private val API_KEY = BuildConfig.CLOUDINARY_API_KEY
        private val API_SECRET = BuildConfig.CLOUDINARY_API_SECRET
    }

    override suspend fun uploadImage(imageUri: Uri): String {
        val file = uriToFile(imageUri)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val timestamp = (System.currentTimeMillis() / 1000).toString()
        val signature = generateSignature(timestamp)

        val apiKeyBody = API_KEY.toRequestBody("text/plain".toMediaTypeOrNull())
        val timestampBody = timestamp.toRequestBody("text/plain".toMediaTypeOrNull())
        val signatureBody = signature.toRequestBody("text/plain".toMediaTypeOrNull())

        val response = cloudinaryApi.uploadImage(
            cloudName = CLOUD_NAME,
            file = filePart,
            apiKey = apiKeyBody,
            timestamp = timestampBody,
            signature = signatureBody
        )
        return response.secure_url
    }

    /**
     * Genera la firma SHA-1 requerida por Cloudinary para signed uploads.
     * Formato: SHA1("timestamp=<timestamp><api_secret>")
     */
    private fun generateSignature(timestamp: String): String {
        val toSign = "timestamp=$timestamp$API_SECRET"
        val digest = MessageDigest.getInstance("SHA-1")
        val hashBytes = digest.digest(toSign.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("No se pudo abrir la imagen")
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        inputStream.close()
        return tempFile
    }
}
