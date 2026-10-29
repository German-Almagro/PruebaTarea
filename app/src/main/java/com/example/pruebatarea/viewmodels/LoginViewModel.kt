package com.example.pruebatarea.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebatarea.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // Estado reactivo expuesto como StateFlow de solo lectura
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Eventos de un solo uso expuestos mediante Channel / Flow
    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { currentState ->
            val emailError = validateEmail(email)
            currentState.copy(
                email = email,
                emailError = emailError,
                isLoginEnabled = isFormValid(email, currentState.password, emailError, currentState.passwordError)
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { currentState ->
            val passwordError = validatePassword(password)
            currentState.copy(
                password = password,
                passwordError = passwordError,
                isLoginEnabled = isFormValid(currentState.email,
                    password, currentState.emailError, passwordError)
            )
        }
    }

    fun onLoginClicked() {

        val currentState = _uiState.value

        if (!currentState.isLoginEnabled || currentState.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Simulación de llamada a API/Repositorio de autenticación
            val isSuccess = simulateApiCall(currentState.email, currentState.password)

            _uiState.update { it.copy(isLoading = false) }

            if (isSuccess) {
                _uiEvent.send(LoginUiEvent.NavigateToHome)
            } else {
                _uiEvent.send(LoginUiEvent
                    .ShowError(R.string.error_invalid_credentials))
            }
        }
    }

    // Reglas de validación
    private fun validateEmail(email: String): Int? {
        return when {
            email.isEmpty() -> null // No mostrar error si está vacío al escribir
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

    private fun isFormValid(
        email: String,
        password: String,
        emailError: Int?,
        passwordError: Int?
    ): Boolean {
        return email.isNotBlank() &&
                password.isNotBlank() &&
                emailError == null &&
                passwordError == null
    }

    private suspend fun simulateApiCall(email: String, password: String): Boolean {
        kotlinx.coroutines.delay(1500) // Simula latencia de red
        return email == "admin@test.com" && password == "123456"
    }
}