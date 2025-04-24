package com.ravikantsharma.auth.presentation.register

import com.ravikantsharma.auth.presentation.navigation.model.CreatePinData

sealed interface RegisterEvent {
    data object UsernameTaken : RegisterEvent
    data object SuccessfulRegistration : RegisterEvent
    data object NavigateToRegisterScreen : RegisterEvent
    data class NavigateToPinScreen(val screenData: CreatePinData) : RegisterEvent
    data object IncorrectUsername : RegisterEvent
}