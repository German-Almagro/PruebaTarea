package com.example.pruebatarea.viewmodels

sealed interface RegistroUiEvent {
    object NavigateToHome : RegistroUiEvent
    data class ShowError(val messageResId: Int) : RegistroUiEvent
}
