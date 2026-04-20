package com.example.demoapp.features.achievements

import androidx.lifecycle.ViewModel
import com.example.demoapp.domain.model.Achievement
import com.example.demoapp.domain.repository.AchievementRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para la pantalla de logros.
 * Obtiene los logros del usuario actualmente logueado.
 */
@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    /** Obtiene la lista de logros del usuario actual con su estado de progreso. */
    fun getAchievements(): List<Achievement> {
        val userId = userRepository.currentUser.value?.id ?: return emptyList()
        return achievementRepository.getAchievementsForUser(userId)
    }

    /** Cantidad de logros desbloqueados. */
    fun getUnlockedCount(): Int = getAchievements().count { it.isUnlocked }

    /** Total de logros disponibles. */
    fun getTotalCount(): Int = getAchievements().size
}
