package com.example.demoapp.features.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.R
import com.example.demoapp.core.utils.ResourceProvider
import com.example.demoapp.core.utils.ValidatedField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyCodeViewModel @Inject constructor(
    private val resources: ResourceProvider
) : ViewModel() {

    val code = ValidatedField("") { value ->
        when {
            value.isEmpty()  -> resources.getString(R.string.error_code_empty)
            value.length < 4 -> resources.getString(R.string.error_code_invalid)
            else -> null
        }
    }

    var verifyResult by mutableStateOf<Boolean?>(null)
        private set

    fun verifyCode() {
        verifyResult = code.isValid
    }

    fun resetResult() {
        verifyResult = null
    }
}
