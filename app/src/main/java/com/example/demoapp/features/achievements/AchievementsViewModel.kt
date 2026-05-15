package com.example.demoapp.features.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.domain.model.Achievement
import com.example.demoapp.domain.repository.AchievementRepository
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    init {
        loadAchievements()
    }

    private fun loadAchievements() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId() ?: return@launch
            _achievements.value = achievementRepository.getAchievementsForUser(userId)
        }
    }

    fun getAchievements(): List<Achievement> = _achievements.value
    fun getUnlockedCount(): Int = _achievements.value.count { it.isUnlocked }
    fun getTotalCount(): Int = _achievements.value.size
}
