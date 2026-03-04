package com.example.demoapp.features.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ValidatedField

class RegisterViewModel : ViewModel() {

    val fullName = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El nombre completo es obligatorio"
            value.trim().length < 3 -> "Ingresa un nombre válido"
            !value.trim().contains(" ") -> "Ingresa nombre y apellido"
            else -> null
        }
    }

    val email = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El email es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Ingresa un email válido"
            else -> null
        }
    }

    val password = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "La contraseña es obligatoria"
            value.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }

    val confirmPassword = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "Confirma tu contraseña"
            value != password.value -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    val phone = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El teléfono es obligatorio"
            !Patterns.PHONE.matcher(value).matches() -> "Ingresa un teléfono válido"
            value.length < 7 -> "El teléfono debe tener al menos 7 dígitos"
            else -> null
        }
    }

    val location = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "La ubicación es obligatoria"
            value.trim().length < 3 -> "Ingresa una ubicación válida"
            else -> null
        }
    }

    val isFormValid: Boolean
        get() = fullName.isValid
                && email.isValid
                && password.isValid
                && confirmPassword.isValid
                && phone.isValid
                && location.isValid

    fun resetForm() {
        fullName.reset()
        email.reset()
        password.reset()
        confirmPassword.reset()
        phone.reset()
        location.reset()
    }
}