package com.example.demoapp.features.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.data.SessionManager

/**
 * ViewModel que gestiona el estado del formulario de inicio de sesión.
 * Maneja la validación de campos y la lógica de autenticación en memoria.
 */
class LoginViewModel : ViewModel() {

    // Campo de email con validación
    val email = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El email es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Ingresa un email válido"
            else -> null
        }
    }

    // Campo de contraseña con validación
    val password = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "La contraseña es obligatoria"
            value.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }

    // Resultado del intento de login (null = no se ha intentado, true = éxito, false = error)
    var loginResult by mutableStateOf<Boolean?>(null)
        private set

    // Indica si el formulario es válido para enviar
    val isFormValid: Boolean
        get() = email.isValid && password.isValid

    /**
     * Intenta iniciar sesión con las credenciales ingresadas.
     * Usa el SessionManager para validar contra los usuarios en memoria.
     */
    fun login() {
        loginResult = SessionManager.login(email.value, password.value)
    }

    /**
     * Resetea el resultado del login (útil después de mostrar un mensaje de error).
     */
    fun resetLoginResult() {
        loginResult = null
    }

    /**
     * Resetea todos los campos del formulario.
     */
    fun resetForm() {
        email.reset()
        password.reset()
        loginResult = null
    }
}