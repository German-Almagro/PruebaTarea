package com.example.pruebatarea.viewmodels

data class LoginUiState(
    val email: String = "",
    val emailError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null,
    val isLoading: Boolean = false,
    val isLoginEnabled: Boolean = false
)
