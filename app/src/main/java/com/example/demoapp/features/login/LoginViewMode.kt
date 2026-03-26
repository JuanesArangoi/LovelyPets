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
 * ViewModel que gestiona el estado del formulario de inicio de sesión.
 * Usa @HiltViewModel para inyectar el UserRepository via Hilt.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Exponer el usuario actual para determinar el rol después del login
    val currentUser = userRepository.currentUser

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

    // Resultado del intento de login
    var loginResult by mutableStateOf<Boolean?>(null)
        private set

    // Indica si el formulario es válido
    val isFormValid: Boolean
        get() = email.isValid && password.isValid

    /**
     * Intenta iniciar sesión con las credenciales ingresadas.
     * Usa el UserRepository para validar contra los usuarios en memoria.
     */
    fun login() {
        val user = userRepository.login(email.value, password.value)
        loginResult = user != null
    }

    /**
     * Resetea el resultado del login.
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