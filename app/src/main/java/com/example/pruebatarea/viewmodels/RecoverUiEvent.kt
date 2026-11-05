package com.example.pruebatarea.viewmodels

sealed interface RecoverUiEvent {
    object NavigateToHome : RecoverUiEvent
    data class ShowError(val messageResId: Int) : RecoverUiEvent
}
