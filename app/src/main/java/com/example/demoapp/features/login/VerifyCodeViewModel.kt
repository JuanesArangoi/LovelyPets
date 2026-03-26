package com.example.demoapp.features.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ValidatedField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para la pantalla de verificación de código.
 * Valida el código ingresado por el usuario.
 */
@HiltViewModel
class VerifyCodeViewModel @Inject constructor() : ViewModel() {

    val code = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El código es obligatorio"
            value.length < 4 -> "El código debe tener al menos 4 caracteres"
            else -> null
        }
    }

    // Resultado de la verificación
    var verifyResult by mutableStateOf<Boolean?>(null)
        private set

    /**
     * Simula la verificación del código.
     * En fase 2, siempre se considera exitoso si el código es válido.
     */
    fun verifyCode() {
        verifyResult = code.isValid
    }

    fun resetResult() {
        verifyResult = null
    }
}
