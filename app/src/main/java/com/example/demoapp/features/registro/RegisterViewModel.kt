package com.example.demoapp.features.registro

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.R
import com.example.demoapp.core.utils.ResourceProvider
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.model.User
import com.example.demoapp.domain.model.UserRole
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel que gestiona el formulario de registro de nuevo usuario.
 * Ahora usa coroutines para Firebase Auth + Firestore.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val resources: ResourceProvider
) : ViewModel() {

    val fullName = ValidatedField("") { value ->
        when {
            value.isEmpty() -> resources.getString(R.string.error_name_empty)
            value.trim().length < 3 -> resources.getString(R.string.error_name_short)
            !value.trim().contains(" ") -> resources.getString(R.string.error_name_no_lastname)
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
            value.isEmpty() -> resources.getString(R.string.error_password_empty)
            value.length < 6 -> resources.getString(R.string.error_password_short)
            else -> null
        }
    }

    val confirmPassword = ValidatedField("") { value ->
        when {
            value.isEmpty() -> resources.getString(R.string.error_confirm_password_empty)
            value != password.value -> resources.getString(R.string.error_confirm_password_mismatch)
            else -> null
        }
    }

    val phone = ValidatedField("") { value ->
        when {
            value.isEmpty() -> resources.getString(R.string.error_phone_empty)
            !Patterns.PHONE.matcher(value).matches() -> resources.getString(R.string.error_phone_invalid)
            value.length < 7 -> resources.getString(R.string.error_phone_short)
            else -> null
        }
    }

    val location = ValidatedField("") { value ->
        when {
            value.isEmpty() -> resources.getString(R.string.error_city_empty)
            value.trim().length < 3 -> resources.getString(R.string.error_city_short)
            else -> null
        }
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

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
     * Registra un nuevo usuario con Firebase Auth y Firestore.
     */
    fun register() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val newUser = User(
                    name = fullName.value,
                    email = email.value,
                    phoneNumber = phone.value,
                    city = location.value
                )
                userRepository.save(newUser, password.value)

                // Obtener el ID asignado por Firebase Auth
                val userId = userRepository.getCurrentUserId() ?: throw Exception("Error al obtener usuario")
                registerResult = Pair(userId, UserRole.USUARIO)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error al registrar"
                registerResult = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetRegisterResult() {
        registerResult = null
        _errorMessage.value = null
    }

    fun resetForm() {
        fullName.reset(); email.reset(); password.reset()
        confirmPassword.reset(); phone.reset(); location.reset()
        registerResult = null
        _errorMessage.value = null
    }
}