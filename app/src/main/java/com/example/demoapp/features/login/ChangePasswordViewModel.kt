package com.example.demoapp.features.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para la pantalla de cambio de contraseña.
 * Valida la nueva contraseña y su confirmación.
 */
@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val newPassword = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "La contraseña es obligatoria"
            value.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }

    val confirmPassword = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "Confirma tu contraseña"
            value != newPassword.value -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    // Resultado del cambio de contraseña
    var changeResult by mutableStateOf<Boolean?>(null)
        private set

    val isFormValid: Boolean
        get() = newPassword.isValid && confirmPassword.isValid

    /**
     * Simula el cambio de contraseña.
     */
    fun changePassword() {
        changeResult = isFormValid
    }

    fun resetResult() {
        changeResult = null
    }
}
