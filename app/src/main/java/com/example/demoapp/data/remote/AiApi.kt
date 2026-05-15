package com.example.demoapp.data.remote

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * API de Groq para el asistente IA de cuidado de mascotas.
 * Compatible con el formato OpenAI Chat Completions.
 */
interface AiApi {

    @POST("openai/v1/chat/completions")
    suspend fun chatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: AiChatRequest
    ): AiChatResponse
}

// --- Modelos de Request/Response ---

data class AiChatRequest(
    val model: String = "llama-3.3-70b-versatile",
    val messages: List<AiMessage>,
    val max_tokens: Int = 1024,
    val temperature: Double = 0.7
)

data class AiMessage(
    val role: String,   // "system", "user", "assistant"
    val content: String
)

data class AiChatResponse(
    val choices: List<AiChoice> = emptyList()
)

data class AiChoice(
    val message: AiMessage? = null
)
