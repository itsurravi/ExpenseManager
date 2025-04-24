package com.ravikantsharma.auth.presentation.create_pin

import com.ravikantsharma.auth.presentation.navigation.model.CreatePinData

sealed interface CreatePinEvent {
    data class NavigateToConfirmPinScreen(val screenData: CreatePinData) : CreatePinEvent
    data object NavigateToRegisterScreen : CreatePinEvent
    data object NavigateToPreferencesScreen : CreatePinEvent
    data object PinsDoNotMatch : CreatePinEvent
}