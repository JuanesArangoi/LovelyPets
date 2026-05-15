package com.example.demoapp.features.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.core.utils.ResourceProvider
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.model.UserRole
import com.example.demoapp.domain.repository.UserRepository
import com.example.demoapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel que gestiona el estado del formulario de inicio de sesión.
 * Ahora usa coroutines para las operaciones asíncronas de Firebase Auth.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val resources: ResourceProvider
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Boolean?>(null)
    val loginResult: StateFlow<Boolean?> = _loginResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Campo de email con validación
    val email = ValidatedField("") { value ->
        when {
            value.isEmpty() -> resources.getString(R.string.error_email_empty)
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> resources.getString(R.string.error_email_invalid)
            else -> null
        }
    }

    // Campo de contraseña con validación
    val password = ValidatedField("") { value ->
        when {
            value.isEmpty() -> resources.getString(R.string.error_password_empty)
            value.length < 6 -> resources.getString(R.string.error_password_short)
            else -> null
        }
    }

    val isFormValid: Boolean
        get() = email.isValid && password.isValid

    /**
     * Intenta iniciar sesión con Firebase Auth.
     * Retorna el par (userId, role) si es exitoso.
     */
    fun login(onSuccess: (String, UserRole) -> Unit) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val user = userRepository.login(email.value, password.value)
                if (user != null) {
                    _loginResult.value = true
                    onSuccess(user.id, user.role)
                } else {
                    _loginResult.value = false
                    _errorMessage.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _loginResult.value = false
                _errorMessage.value = e.message ?: "Error al iniciar sesión"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetLoginResult() {
        _loginResult.value = null
        _errorMessage.value = null
    }

    fun resetForm() {
        email.reset()
        password.reset()
        _loginResult.value = null
        _errorMessage.value = null
    }
}