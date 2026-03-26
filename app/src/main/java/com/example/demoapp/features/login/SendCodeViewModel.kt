package com.example.demoapp.features.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para la pantalla de envío de código de recuperación.
 * Valida el email y simula el envío de un código.
 */
@HiltViewModel
class SendCodeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val email = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El email es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Ingresa un email válido"
            else -> null
        }
    }

    // Resultado del envío (null = no intentado, true = éxito, false = email no encontrado)
    var sendResult by mutableStateOf<Boolean?>(null)
        private set

    /**
     * Simula el envío de un código de verificación al email.
     */
    fun sendCode() {
        // En fase 2, siempre se considera exitoso si el email es válido
        sendResult = email.isValid
    }

    fun resetResult() {
        sendResult = null
    }
}
