package com.example.demoapp.domain.repository

/**
 * Repositorio para el asistente IA de cuidado de mascotas.
 */
interface AiRepository {
    suspend fun askQuestion(prompt: String): String
}
