package com.example.demoapp.features.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.R
import com.example.demoapp.core.utils.ResourceProvider
import com.example.demoapp.core.utils.ValidatedField
import com.example.demoapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val resources: ResourceProvider
) : ViewModel() {

    val newPassword = ValidatedField("") { value ->
        when {
            value.isEmpty()  -> resources.getString(R.string.error_password_empty)
            value.length < 6 -> resources.getString(R.string.error_password_short)
            else -> null
        }
    }

    val confirmPassword = ValidatedField("") { value ->
        when {
            value.isEmpty()         -> resources.getString(R.string.error_confirm_password_empty)
            value != newPassword.value -> resources.getString(R.string.error_confirm_password_mismatch)
            else -> null
        }
    }

    var changeResult by mutableStateOf<Boolean?>(null)
        private set

    val isFormValid: Boolean
        get() = newPassword.isValid && confirmPassword.isValid

    fun changePassword() {
        changeResult = isFormValid
    }

    fun resetResult() {
        changeResult = null
    }
}
