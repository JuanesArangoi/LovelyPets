package com.example.demoapp.data.repository

import com.example.demoapp.BuildConfig
import com.example.demoapp.data.remote.AiApi
import com.example.demoapp.data.remote.AiChatRequest
import com.example.demoapp.data.remote.AiMessage
import com.example.demoapp.domain.repository.AiRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio IA usando Groq API.
 * Envía prompts del usuario con un system prompt enfocado en mascotas.
 */
@Singleton
class AiRepositoryImpl @Inject constructor(
    private val aiApi: AiApi
) : AiRepository {

    companion object {
        private val API_KEY = BuildConfig.GROQ_API_KEY
        private const val SYSTEM_PROMPT = """Eres PetCare AI, un asistente experto en cuidado de mascotas para la app LovelyPets. 
Tu rol es ayudar a los usuarios con preguntas sobre:
- Alimentación y nutrición de mascotas
- Salud y primeros auxilios veterinarios
- Comportamiento y entrenamiento
- Cuidados generales (higiene, ejercicio, etc.)
- Consejos para adopción responsable
- Información sobre razas y especies

Responde siempre en español, de forma clara, amigable y concisa. 
Si la pregunta no está relacionada con mascotas, indica amablemente que solo puedes ayudar con temas de mascotas.
Usa emojis ocasionalmente para hacer las respuestas más amigables 🐾"""
    }

    override suspend fun askQuestion(prompt: String): String {
        return try {
            val request = AiChatRequest(
                messages = listOf(
                    AiMessage(role = "system", content = SYSTEM_PROMPT),
                    AiMessage(role = "user", content = prompt)
                )
            )
            val response = aiApi.chatCompletion(
                authorization = "Bearer $API_KEY",
                request = request
            )
            response.choices.firstOrNull()?.message?.content
                ?: "No se pudo obtener una respuesta. Inténtalo de nuevo."
        } catch (e: Exception) {
            "Error al conectar con el asistente: ${e.message}"
        }
    }
}
