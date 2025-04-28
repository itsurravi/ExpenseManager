package com.ravikantsharma.auth.presentation.user_preference

sealed interface OnboardingPreferencesEvent {
    data object NavigateToRegisterScreen : OnboardingPreferencesEvent
    data object NavigateToDashboardScreen : OnboardingPreferencesEvent

    sealed interface Error: OnboardingPreferencesEvent {
        data object Generic: Error
        data object DuplicateEntry: Error
    }
}