package com.example.demoapp.domain.repository

import com.example.demoapp.domain.model.Achievement

/**
 * Interfaz del repositorio de logros.
 * Define la operación para obtener los logros de un usuario específico.
 */
interface AchievementRepository {
    /**
     * Obtiene la lista de logros con su estado de desbloqueo para un usuario dado.
     * @param userId Identificador del usuario
     * @return Lista de logros con progreso actualizado
     */
    fun getAchievementsForUser(userId: String): List<Achievement>
}
