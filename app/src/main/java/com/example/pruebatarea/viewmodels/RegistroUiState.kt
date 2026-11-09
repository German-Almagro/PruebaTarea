package com.example.pruebatarea.viewmodels

data class RegistroUiState(
    val email: String = "",
    val emailError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: Int? = null,
    val isLoading: Boolean = false,
    val isRegistroEnabled: Boolean = false
)