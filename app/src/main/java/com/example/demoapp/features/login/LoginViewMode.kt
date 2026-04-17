package com.example.demoapp.features.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ResourceProvider
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.data.datastore.SessionDataStore
import com.example.demoapp.domain.model.UserRole
import com.example.demoapp.domain.repository.UserRepository
import com.example.demoapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel que gestiona el estado del formulario de inicio de sesión.
 * Usa @HiltViewModel para inyectar el UserRepository, SessionDataStore y ResourceProvider via Hilt.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionDataStore: SessionDataStore,
    private val resources: ResourceProvider
) : ViewModel() {

    // Resultado del login: null=sin resultado, true=exitoso, false=fallido
    private val _loginResult = MutableStateFlow<Boolean?>(null)
    val loginResult: StateFlow<Boolean?> = _loginResult.asStateFlow()

    // Usuario logueado (para obtener su ID y rol después del login)
    val currentUser = userRepository.currentUser

    // Campo de email con validación usando ResourceProvider
    val email = ValidatedField("") { value ->
        when {
            value.isEmpty() -> resources.getString(R.string.error_email_empty)
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> resources.getString(R.string.error_email_invalid)
            else -> null
        }
    }

    // Campo de contraseña con validación usando ResourceProvider
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
     * Intenta iniciar sesión. Si es exitoso, guarda la sesión en DataStore.
     * La UI reacciona al sessionFlow de DataStore, cambiando la navegación automáticamente.
     */
    suspend fun login(): Pair<String, UserRole>? {
        val user = userRepository.login(email.value, password.value)
        _loginResult.value = user != null
        return user?.let { Pair(it.id, it.role) }
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }

    fun resetForm() {
        email.reset()
        password.reset()
        _loginResult.value = null
    }
}