package com.ravikantsharma.auth.presentation.register

import com.ravikantsharma.ui.navigation.CreatePinScreenData

sealed interface RegisterEvent {
    data object UsernameTaken : RegisterEvent
    data object SuccessfulRegistration : RegisterEvent
    data object NavigateToLoginScreen : RegisterEvent
    data class NavigateToPinScreen(val screenData: CreatePinScreenData) : RegisterEvent
    data object IncorrectUsername : RegisterEvent
    data object DuplicateUsername : RegisterEvent
}