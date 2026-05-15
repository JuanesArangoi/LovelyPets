package com.example.demoapp.core.utils

/**
 * Clase sellada que representa los posibles resultados de una solicitud asíncrona.
 * Se usa en ViewModels para manejar estados de Loading, Success y Failure.
 */
sealed class RequestResult {
    data class Success(val message: String) : RequestResult()
    data class Failure(val errorMessage: String) : RequestResult()
    data object Loading : RequestResult()
}
