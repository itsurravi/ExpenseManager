package com.ravikantsharma.auth.presentation.register

import com.ravikantsharma.auth.presentation.navigation.model.CreatePinScreenData

sealed interface RegisterEvent {
    data object UsernameTaken : RegisterEvent
    data object SuccessfulRegistration : RegisterEvent
    data object NavigateToRegisterScreen : RegisterEvent
    data class NavigateToPinScreen(val screenData: CreatePinScreenData) : RegisterEvent
    data object IncorrectUsername : RegisterEvent
    data object DuplicateUsername : RegisterEvent
}