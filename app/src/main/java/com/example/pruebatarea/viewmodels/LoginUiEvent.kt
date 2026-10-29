package com.example.pruebatarea.viewmodels

sealed interface LoginUiEvent {
    object NavigateToHome : LoginUiEvent
    data class ShowError(val messageResId: Int) : LoginUiEvent
}
