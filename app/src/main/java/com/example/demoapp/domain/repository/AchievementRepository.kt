package com.example.demoapp.domain.repository

import com.example.demoapp.domain.model.Achievement

/**
 * Interfaz del repositorio de logros.
 * Suspend para compatibilidad con Firestore.
 */
interface AchievementRepository {
    suspend fun getAchievementsForUser(userId: String): List<Achievement>
}
