package com.example.demoapp.features.registro

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.R
import com.example.demoapp.core.utils.ResourceProvider
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.model.User
import com.example.demoapp.domain.model.UserRole
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel que gestiona el formulario de registro de nuevo usuario.
 * Usa @HiltViewModel para inyectar el UserRepository y ResourceProvider via Hilt.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val resources: ResourceProvider
) : ViewModel() {

    val fullName = ValidatedField("") { value ->
        when {
            value.isEmpty()                   -> resources.getString(R.string.error_name_empty)
            value.trim().length < 3           -> resources.getString(R.string.error_name_short)
            !value.trim().contains(" ")       -> resources.getString(R.string.error_name_no_lastname)
            else -> null
        }
    }

    val email = ValidatedField("") { value ->
        when {
            value.isEmpty() -> resources.getString(R.string.error_email_empty)
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> resources.getString(R.string.error_email_invalid)
            else -> null
        }
    }

    val password = ValidatedField("") { value ->
        when {
            value.isEmpty()  -> resources.getString(R.string.error_password_empty)
            value.length < 6 -> resources.getString(R.string.error_password_short)
            else -> null
        }
    }

    val confirmPassword = ValidatedField("") { value ->
        when {
            value.isEmpty()        -> resources.getString(R.string.error_confirm_password_empty)
            value != password.value -> resources.getString(R.string.error_confirm_password_mismatch)
            else -> null
        }
    }

    val phone = ValidatedField("") { value ->
        when {
            value.isEmpty()                              -> resources.getString(R.string.error_phone_empty)
            !Patterns.PHONE.matcher(value).matches()    -> resources.getString(R.string.error_phone_invalid)
            value.length < 7                             -> resources.getString(R.string.error_phone_short)
            else -> null
        }
    }

    val location = ValidatedField("") { value ->
        when {
            value.isEmpty()        -> resources.getString(R.string.error_city_empty)
            value.trim().length < 3 -> resources.getString(R.string.error_city_short)
            else -> null
        }
    }

    var registerResult by mutableStateOf<Pair<String, UserRole>?>(null)
        private set

    val isFormValid: Boolean
        get() = fullName.isValid
                && email.isValid
                && password.isValid
                && confirmPassword.isValid
                && phone.isValid
                && location.isValid

    /**
     * Registra un nuevo usuario. Si el registro es exitoso, devuelve (userId, UserRole)
     * para que la pantalla pueda guardar la sesión en DataStore.
     */
    fun register() {
        val newUser = User(
            id          = UUID.randomUUID().toString(),
            name        = fullName.value,
            email       = email.value,
            password    = password.value,
            phoneNumber = phone.value,
            city        = location.value
        )
        val success = userRepository.register(newUser)
        registerResult = if (success) Pair(newUser.id, newUser.role) else null
    }

    fun resetRegisterResult() { registerResult = null }

    fun resetForm() {
        fullName.reset(); email.reset(); password.reset()
        confirmPassword.reset(); phone.reset(); location.reset()
        registerResult = null
    }
}