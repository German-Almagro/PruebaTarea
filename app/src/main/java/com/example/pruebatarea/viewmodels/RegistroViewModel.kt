package com.example.pruebatarea.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebatarea.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<RegistroUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { currentState ->
            val emailError = validateEmail(email)
            currentState.copy(
                email = email,
                emailError = emailError,
                isRegistroEnabled = isFormValid(
                    email, currentState.password, currentState.confirmPassword,
                    emailError, currentState.passwordError, currentState.confirmPasswordError
                )
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { currentState ->
            val passwordError = validatePassword(password)
            // Validamos también la confirmación por si ya tenía texto escrito
            val confirmError = validateConfirmPassword(password, currentState.confirmPassword)
            currentState.copy(
                password = password,
                passwordError = passwordError,
                confirmPasswordError = confirmError,
                isRegistroEnabled = isFormValid(
                    currentState.email, password, currentState.confirmPassword,
                    currentState.emailError, passwordError, confirmError
                )
            )
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { currentState ->
            val confirmError = validateConfirmPassword(currentState.password, confirmPassword)
            currentState.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = confirmError,
                isRegistroEnabled = isFormValid(
                    currentState.email, currentState.password, confirmPassword,
                    currentState.emailError, currentState.passwordError, confirmError
                )
            )
        }
    }

    fun onRegistroClicked() {
        val currentState = _uiState.value

        if (!currentState.isRegistroEnabled || currentState.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }


            val isSuccess = simulateApiCall(currentState.email)

            _uiState.update { it.copy(isLoading = false) }

            if (isSuccess) {
                _uiEvent.send(RegistroUiEvent.NavigateToHome)
            } else {
                _uiEvent.send(RegistroUiEvent.ShowError(R.string.error_invalid_credentials))
            }
        }
    }

    private fun validateEmail(email: String): Int? {
        return when {
            email.isEmpty() -> null
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.error_invalid_email
            else -> null
        }
    }

    private fun validatePassword(password: String): Int? {
        return when {
            password.isEmpty() -> null
            password.length < 6 -> R.string.error_password_too_short
            else -> null
        }
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): Int? {
        return when {
            confirmPassword.isEmpty() -> null
            password != confirmPassword -> R.string.error_contraseña_diferente // (Asegúrate de tener este string o usa uno genérico)
            else -> null
        }
    }

    private fun isFormValid(
        email: String,
        password: String,
        confirmPassword: String,
        emailError: Int?,
        passwordError: Int?,
        confirmPasswordError: Int?
    ): Boolean {
        return email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword &&
                emailError == null &&
                passwordError == null &&
                confirmPasswordError == null
    }

    private suspend fun simulateApiCall(email: String): Boolean {
        delay(1500) // Simula latencia de red
        return email == "admin@test.com"
    }
}