package com.ravikantsharma.auth.presentation.create_pin

import com.ravikantsharma.ui.navigation.CreatePinScreenData
import com.ravikantsharma.ui.navigation.PreferencesScreenData

sealed interface CreatePinEvent {
    data class NavigateToConfirmPinScreen(val screenData: CreatePinScreenData) : CreatePinEvent
    data object OnBackClick : CreatePinEvent
    data class NavigateToPreferencesScreen(val screenData: PreferencesScreenData) : CreatePinEvent
    data object PinsDoNotMatch : CreatePinEvent
}