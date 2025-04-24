package com.ravikantsharma.auth.presentation.user_preference

sealed interface OnboardingPreferencesEvent {
    data object NavigateToCreatePinScreen : OnboardingPreferencesEvent
    data object NavigateToDashboardScreen : OnboardingPreferencesEvent
}