package com.example.demoapp.features.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.R
import com.example.demoapp.core.utils.ResourceProvider
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para enviar correo de recuperación de contraseña con Firebase Auth.
 */
@HiltViewModel
class SendCodeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val resources: ResourceProvider
) : ViewModel() {

    val email = ValidatedField("") { value ->
        when {
            value.isEmpty() -> resources.getString(R.string.error_email_empty)
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> resources.getString(R.string.error_email_invalid)
            else -> null
        }
    }

    var sendResult by mutableStateOf<Boolean?>(null)
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Envía un correo de recuperación de contraseña con Firebase Auth.
     */
    fun sendCode() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                userRepository.sendPasswordResetEmail(email.value)
                sendResult = true
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error al enviar el correo"
                sendResult = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetResult() {
        sendResult = null
        _errorMessage.value = null
    }
}
