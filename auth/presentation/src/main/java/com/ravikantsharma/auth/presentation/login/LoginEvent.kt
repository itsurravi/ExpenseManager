package com.ravikantsharma.auth.presentation.login

sealed interface LoginEvent {
    data object IncorrectCredentials : LoginEvent
    data object NavigateToRegisterScreen : LoginEvent
    data object NavigateToDashboardScreen : LoginEvent
}