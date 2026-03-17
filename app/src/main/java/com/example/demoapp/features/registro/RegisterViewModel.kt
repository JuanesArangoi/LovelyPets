package com.example.demoapp.features.registro

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.data.SessionManager
import com.example.demoapp.domain.model.User
import java.util.UUID

/**
 * ViewModel que gestiona el formulario de registro de nuevo usuario.
 * Valida los campos y registra al usuario en SessionManager.
 */
class RegisterViewModel : ViewModel() {

    // Campo: Nombre completo
    val fullName = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El nombre completo es obligatorio"
            value.trim().length < 3 -> "Ingresa un nombre válido"
            !value.trim().contains(" ") -> "Ingresa nombre y apellido"
            else -> null
        }
    }

    // Campo: Email
    val email = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El email es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Ingresa un email válido"
            else -> null
        }
    }

    // Campo: Contraseña
    val password = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "La contraseña es obligatoria"
            value.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }

    // Campo: Confirmar contraseña
    val confirmPassword = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "Confirma tu contraseña"
            value != password.value -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    // Campo: Teléfono
    val phone = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El teléfono es obligatorio"
            !Patterns.PHONE.matcher(value).matches() -> "Ingresa un teléfono válido"
            value.length < 7 -> "El teléfono debe tener al menos 7 dígitos"
            else -> null
        }
    }

    // Campo: Ubicación (ciudad)
    val location = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "La ciudad es obligatoria"
            value.trim().length < 3 -> "Ingresa una ciudad válida"
            else -> null
        }
    }

    // Resultado del registro (null = no intentado, true = éxito, false = error)
    var registerResult by mutableStateOf<Boolean?>(null)
        private set

    // Indica si el formulario es válido
    val isFormValid: Boolean
        get() = fullName.isValid
                && email.isValid
                && password.isValid
                && confirmPassword.isValid
                && phone.isValid
                && location.isValid

    /**
     * Registra un nuevo usuario usando SessionManager.
     */
    fun register() {
        val newUser = User(
            id = UUID.randomUUID().toString(),
            name = fullName.value,
            email = email.value,
            password = password.value,
            phoneNumber = phone.value,
            city = location.value
        )

        registerResult = SessionManager.register(newUser)
    }

    /**
     * Resetea el resultado del registro.
     */
    fun resetRegisterResult() {
        registerResult = null
    }

    /**
     * Resetea todos los campos del formulario.
     */
    fun resetForm() {
        fullName.reset()
        email.reset()
        password.reset()
        confirmPassword.reset()
        phone.reset()
        location.reset()
        registerResult = null
    }
}